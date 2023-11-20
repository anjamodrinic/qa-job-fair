package player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import cards.*;

public class Player {

    private int health;
    private List<Card> hand;
    private List<Card> deck;
    private Card lastPlayedCard;
    private static int initialNumberOfCards = 6;
    private boolean attackingStatus; 
    private int damage;

    public Player(int health, List<Card> deck) {
        this.health = health;
        this.deck = deck;
        this.hand = new ArrayList<>();
        lastPlayedCard = null;
        attackingStatus = false;
        damage = 0;
        shuffleDeck();
    }

    public void takeDamage(int amountOfDamage){
        // Bug fix: Take damage
        health -= amountOfDamage;
    }



    public boolean getAttackingStatus(){
        return attackingStatus;
    }

    public void resetAttackingStatus(){
        attackingStatus = false;
    }

    public int getDamage(){
        return damage;
    }

    public void resetDamage(){
        // Bug fix: Reset damage
        damage = 0;
    }

    public int getHealth() {
        return health;
    }

    public List<Card> getHand() {
        return hand;
    }

    public List<Card> getDeck() {
        return deck;
    }

      public Card getLastPlayedCard() {
        return lastPlayedCard;
    }

      public int getInitialNumberOfCards() {
        return initialNumberOfCards;
    }

    public void shuffleDeck() {
        // Bug fix: Non-random order of cards
        Collections.shuffle(this.deck);
    }

    public void populateDeck(List<Card> cardList) {
        deck.addAll(cardList);
    }

    public int getNumberOfCardsInDeck(){
        return deck.size();
    }

    public int getNumberOfCardsInHand(){
        return hand.size();
    }

    public void drawCard() {
        // Bug fix: Draw card from an empty deck

        if(!deck.isEmpty()){
            Card drawnCard = deck.remove(deck.size() - 1);
            hand.add(drawnCard);
        }
        else{
            System.out.println("Deck is empty, play with remaining cards.");
        }
    }

    public void drawInitialCards() {
        for (int i = 0; i < initialNumberOfCards; i++) {
            drawCard();
        }
    }

    public void playCard(int cardNumber) {
        Card cardToPlay = null;
        for (Card card : hand) {
            if (card.getNumber() == cardNumber) {
                cardToPlay = card;
                break;
            }
        }
        //Bug fix: Player tries to play card he doesn't have
        try {
            hand.remove(cardToPlay);
            cardToPlay.effect();
        } catch (Exception e) {
            System.out.println("\nYou cannot play card you don't have in hand, please chose another card from hand\n");
        }

         
            lastPlayedCard = cardToPlay;

            if(cardToPlay instanceof AttackCard){
                attackingStatus = true;
                damage += cardToPlay.getNumber();
            }
            if(cardToPlay instanceof BoostAttackCard){
                //Bug fix: Player can attack with boost card
                attackingStatus = false;
                damage += ((BoostAttackCard)cardToPlay).getBoost();
            }

       
    }

    public void playCardInDefense(int cardNumber){
         Card cardToPlay = null;
        for (Card card : hand) {
            if (card.getNumber() == cardNumber) {
                cardToPlay = card;
                break;
            }
        }
        // Bug fix: Opponent can not be attacked with the same card which special ability is used for deflecting the attack
        hand.remove(cardToPlay);

        if(cardToPlay != null){

            lastPlayedCard = cardToPlay;
            //Bug fix: After using special ability of the Attack card, player is attacking
            attackingStatus=false;


            System.out.println(String.format("You've defended yourself! You've been attacked for %d damage and you've used special ability of Attacking card %d to deflect the attack\r\n", cardNumber, cardNumber));
        }
        else {
            System.out.println("You don't have this card in your hand...\r\n");
        }
    }

    public boolean checkForProtectionPossibilitiesInHand(Card lastPlayedCard){
        for (Card card : hand) {

            if (card instanceof ProtectCard || card.getNumber() == lastPlayedCard.getNumber()) {
                return true;
            }
        }
        return false;
    }

    //Bug fix: when we have that card in hand, return true
    public boolean findNumberInHand(int number){
        for(Card card : hand){
            if(card.getNumber() == number){
                return true;
            }
        }
        System.out.println(String.format("There is no card in hand with number %d\r\n", number));
        return false;
    }

    public void printHand() {
        System.out.println("Player's Hand:");
        for (Card card : hand) {
            System.out.println(card.getNumber() + "(" + card.description()+ ")\r");
            // You can add additional details about the card if needed
        }
        System.out.println();
    }
}

