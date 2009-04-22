package ci.textanalysis.lucene.impl;

import java.io.IOException;
import java.util.List;
import java.util.Stack;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

import ci.textanalysis.PhrasesCache;
import ci.textanalysis.SynonymsCache;

public class SynonymPhraseStopWordFilter extends TokenFilter {
    private Stack<Token> injectedTokensStack;
    private Token previousToken;
    private SynonymsCache synonymsCache;
    private PhrasesCache phrasesCache;
    
    public SynonymPhraseStopWordFilter(TokenStream input, SynonymsCache synonymsCache, PhrasesCache phrasesCache) {
        super(input);
        this.synonymsCache = synonymsCache;
        this.phrasesCache = phrasesCache;
        this.injectedTokensStack = new Stack<Token>();
    }
    
    @Override
    public Token next() throws IOException {
        if (this.injectedTokensStack.size() > 0) {
            return this.injectedTokensStack.pop();
        }
        Token token = input.next();
        if (token != null) {
            String phrase = injectPhrases(token);
            injectSynonyms(token.termText(), token);
            injectSynonyms(phrase, token);
            this.previousToken = token;
        }
        return token;
    }

    private String injectPhrases(Token currentToken) throws IOException {
        if (this.previousToken != null) {
            String phrase = this.previousToken.termText() + " "
                + currentToken.termText();
            if (this.phrasesCache.isValidPhrase(phrase)) {
                Token phraseToken = new Token(phrase, currentToken.startOffset(),
                        currentToken.endOffset(), "phrase");
                phraseToken.setPositionIncrement(0);
                this.injectedTokensStack.push(phraseToken);
                return phrase;
            }
        }
        return null;
    }
    
    private void injectSynonyms(String text, Token token) throws IOException {
        if (text != null) {
            List<String> synonyms = this.synonymsCache.getSynonym(text);
            if (synonyms != null) {
                for (String synonym : synonyms) {
                    Token synonymToken = new Token(synonym, token.startOffset(),
                            token.endOffset(), "synonym");
                    synonymToken.setPositionIncrement(0);
                    this.injectedTokensStack.push(synonymToken);
                }
            }
        }
        
    }
    
}
