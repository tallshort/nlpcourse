import htmllib
import formatter
from formatter import AbstractFormatter
import string
import codecs
import StringIO
from os.path import basename, isdir
from os import listdir
from htmlentitydefs import entitydefs
from HTMLParser import HTMLParser
import re
import urllib2,urllib,sys
def extrat_text(html,minDensity):
    # Derive from formatter.AbstractWriter to store paragraphs.
    writer = LineWriter()
    # Default formatter sends commands to our writer.
    formatter = AbstractFormatter(writer)
    # Derive from htmllib.HTMLParser to track parsed bytes.
    parser = TrackingParser(writer, formatter)
    # Give the parser the raw HTML data.
    parser.feed(html)
    parser.close()
    # Filter the paragraphs stored and output them.
    return writer.output(minDensity)
class TrackingParser(htmllib.HTMLParser):
    
    """Try to keep accurate pointer of parsing location."""
    def __init__(self, writer, *args):
        htmllib.HTMLParser.__init__(self, *args)
        self.writer = writer

    def parse_starttag(self, i):
        index = htmllib.HTMLParser.parse_starttag(self, i)
        self.writer.index = index
        return index
    def parse_endtag(self, i):
        self.writer.index = i
        return htmllib.HTMLParser.parse_endtag(self, i)
class TitleParser(HTMLParser):
    def __init__(self):
        self.taglevels = []
        self.handledtags = ['title','ul','li']
        self.processing = None
        HTMLParser.__init__(self)
    def handle_starttag(self,tag,attrs):
            if len(self.taglevels) and self.taglevels[-1] == tag:
                self.handle_endtag(tag)
            self.taglevels.append(tag)
            if tag in self.handledtags:
                self.data = ''
                self.processing = tag
                if tag == 'ul':
                    print "List Started"
    def handle_data(self,data):
           if self.processing:
              self.data += data
    def handle_endtag(self,tag):
           if not tag in self.taglevels:
               return
           while len(self.taglevels):
               starttag =self.taglevels.pop()
               if starttag in self.handledtags:
                   self.finishprocessing(starttag)
               if starttag == tag:
                   break
    def cleanse(self):
          self.data =re.sub('\s+',' ',self.data)
    def finishprocessing(self,tag):
        self.cleanse()
        if tag == 'title' and tag == self.processing:
            print "Doucument Title:",self.data
        elif tag == 'ul':
            print "List ended"
        elif tag == 'li' and tag == self.processing:
            print "List item:", self.data
        self.processing = None
    def handle_entityref(self,name):
        if entitydefs.has_key(name):
            self.handle_data(entitydefs[name])
        else:
            self.handle_data('&' + name + ';') 
    def handle_charref(self,name):
        try:
            charnum = int(name)
        except valueError:
            return
        if charnum < 1 or charnum > 255:
            return
        self.handle_data(chr(charnum))
    def gettitle(self):
        return self.data
    
            
class Paragraph:
    def __init__(self):
        self.text = ''
        self.bytes = 0
        self.density = 0.0
class LineWriter(formatter.AbstractWriter):
    def __init__(self, *args):
        self.last_index = 0
        self.lines = [Paragraph()]
        formatter.AbstractWriter.__init__(self)
    def send_flowing_data(self, data):
        # Work out the length of this text chunk.
        t = len(data)
        # We've parsed more text, so increment index.
        self.index += t
        # Calculate the number of bytes since last time.
        b = self.index - self.last_index
        self.last_index = self.index
        # Accumulate this information in current line.
        l = self.lines[-1]
        l.text += data
        l.bytes += b
    def send_paragraph(self, blankline):
        """Create a new paragraph if necessary."""
        if self.lines[-1].text == '':
            return
        self.lines[-1].text += "\n" * (blankline+1)
        self.lines[-1].bytes += 2 * (blankline+1)
        self.lines.append(Paragraph())
    def send_literal_data(self, data):
        self.send_flowing_data(data)
    def send_line_break(self):
        self.send_paragraph(0)
    def compute_density(self):
        """Calculate the density for each line, and the average."""
        total = 0.0
        for l in self.lines:
            a = float(l.bytes)
            if a!= 0:
                l.density = len(l.text) / float(l.bytes)
                total += l.density
        # Store for optional use by the neural network.
        self.average = total / float(len(self.lines))
    def output(self,minDensity):
        """Return a string with the useless lines filtered out."""
        self.compute_density()
        output = StringIO.StringIO()
        for l in self.lines:
           #print l.text,"====>",l.bytes 
            # Check density against threshold.
            # Custom filter extensions go here.
           if l.density > minDensity:
               # print l.text
                output.write(l.text)
        return output.getvalue()
#------------------------------------------------------------
def zhtounicode(str):
    for c in ('utf-8', 'gbk', 'big5', 'jp', 'kr'):    
        try:
            return str.decode(c)
        except:
            pass
    return str 

#------------------------------------------------------------

def get_data(f):
    return f.read()

try:
    path = sys.argv[2]
    minDensity = float(sys.argv[1])
    
    if minDensity < 0.0 or minDensity > 1.0:
        print "@@@@error@@@@"
    print path
    print minDensity

    f = open(path)
    data = get_data(f)
    f.close()

    pp = re.compile('<style.*?/style>|<script.*?/script>', re.DOTALL)
    data = pp.sub(' ', data)
    p = extrat_text(data, minDensity)
    print ""
    print "@@@@@@@@@@"
    print p
except:
    print "@@@@error@@@@"
