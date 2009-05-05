require 'mofo'

def print_card(card)
  puts card.title if card.properties.member?("title")
  puts card.locality if card.properties.member?("locality")
  puts card.org if card.properties.member?("org")
end

card = hCard.find ARGV[0]

if card.respond_to?:each
  card.each do |c|
    print_card(c)
  end
else
  print_card(card)
end
