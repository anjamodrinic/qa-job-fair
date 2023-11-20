package test;

import cards.AttackCard;
import cards.BoostAttackCard;
import cards.Card;
import cards.ProtectCard;
import org.junit.Test;
import player.Player;
import utility.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TestPlayer {


    @Test
    public void testTakeDamage() {
        new Utility();
        Player player = new Player(10, Utility.generateCards());
        player.takeDamage(1);
        assertEquals(9, player.getHealth());
    }

    @Test
    public void testInitialization() {
        new Utility();

        int health = 20;
        Player player = new Player(health, Utility.generateCards());

        assertEquals(player.getHealth(), health);
        assertEquals(25, player.getNumberOfCardsInDeck());
        assertEquals(0, player.getNumberOfCardsInHand());
        assertEquals(0, player.getDamage());
        assertNull(player.getLastPlayedCard());
        assertFalse(player.getAttackingStatus());
    }

    @Test
    public void testDrawCard() {
        new Utility();

        Player player = new Player(20, Utility.generateCards());

        int initial_deck_size = player.getNumberOfCardsInDeck();
        int initial_hand_size = player.getNumberOfCardsInHand();
        player.drawInitialCards();
        int current_deck_size = player.getNumberOfCardsInDeck();
        int current_hand_size = player.getNumberOfCardsInHand();

        assertEquals(initial_deck_size - 6, current_deck_size);
        assertEquals(initial_hand_size + 6, current_hand_size);

        initial_deck_size = player.getDeck().size();
        initial_hand_size = player.getNumberOfCardsInHand();
        player.drawCard();
        current_deck_size = player.getDeck().size();
        current_hand_size = player.getNumberOfCardsInHand();

        assertEquals(initial_deck_size - 1, current_deck_size);
        assertEquals(initial_hand_size + 1, current_hand_size);
    }




    @Test
    public void resetAttackingStatus() {
        new Utility();

        Player player = new Player(20, Utility.generateCards());
        player.drawInitialCards();
        player.getHand().add(new AttackCard(3));
        player.playCard(3);
        player.getDamage();
        assertEquals(3, player.getDamage());
        assertTrue(player.getAttackingStatus());
        player.resetAttackingStatus();
        assertFalse(player.getAttackingStatus());
    }

    @Test
    public void resetDamage() {
        new Utility();

        Player player = new Player(20, Utility.generateCards());
        player.drawInitialCards();
        player.getHand().add(new AttackCard(3));
        player.playCard(3);
        player.getDamage();
        assertEquals(3, player.getDamage());

        player.resetDamage();
        assertEquals(0, player.getDamage());
    }

    @Test
    public void testShuffleDeck() {
        Player player = new Player(20, Utility.generateCards());
        List<Card> originalDeck = new ArrayList<>(player.getDeck());
        player.shuffleDeck();
        assertFalse(originalDeck.equals(player.getDeck()));
    }


    @Test
    public void drawInitialCards() {
        new Utility();

        Player player = new Player(20, Utility.generateCards());
        player.drawInitialCards();
        assertEquals(6,player.getInitialNumberOfCards());
    }

    @Test
    public void testPopulateDeck() {
        Player player = new Player(20, new ArrayList<>());
        List<Card> cards = Utility.generateCards();
        player.populateDeck(cards);
        assertEquals(cards.size(), player.getDeck().size());
        assertTrue(player.getDeck().containsAll(cards));
    }

    @Test
    public void testPlayCard() {
        Player player = new Player(20, Utility.generateCards());
        player.drawInitialCards();
        int handSizeBeforePlay = player.getHand().size();


        Card cardToPlay = player.getHand().get(0); // Getting the first card in the hand
        player.playCard(cardToPlay.getNumber()); // Playing the first card

        assertFalse(player.getHand().contains(cardToPlay)); // Check if the card is no longer in the hand
        assertEquals(handSizeBeforePlay - 1, player.getHand().size());
    }

    @Test
    public void testGetLastPlayedCard() {
        Player player = new Player(20, Utility.generateCards());
        Card expectedCard = new AttackCard(3);
        player.getHand().add(expectedCard);
        player.playCard(expectedCard.getNumber());
        assertEquals(expectedCard.getNumber(), player.getLastPlayedCard().getNumber());
    }

    @Test
    public void testFindNumberInHand() {
        List<Card> testCards = new ArrayList<>();
        testCards.add(new BoostAttackCard());
        testCards.add(new BoostAttackCard());
        testCards.add(new BoostAttackCard());
        testCards.add(new BoostAttackCard());
        testCards.add(new BoostAttackCard());
        testCards.add(new ProtectCard());
        Player player = new Player(20, testCards);
        player.drawInitialCards();

        assertTrue(player.findNumberInHand(1)); // Known number in hand
        assertFalse(player.findNumberInHand(10)); // Known number not in hand
    }

    @Test
    public void testCheckForProtectionPossibilitiesInHand() {
        // Scenario 1. a): Protection is possible with Protect card
        Player player = new Player(20, Utility.generateCards());
        player.drawInitialCards();
        Card lastPlayedCard = new AttackCard(3);

        // Manually add a protective card to the player's hand for the test
        player.getHand().add(new ProtectCard());

        assertTrue(player.checkForProtectionPossibilitiesInHand(lastPlayedCard));
        // Scenario 1. b): Protection is possible with special ability of Attack card
        player.getHand().removeIf(card -> card instanceof ProtectCard);
        player.getHand().add(new AttackCard(3));

        assertTrue(player.checkForProtectionPossibilitiesInHand(lastPlayedCard));

        // Scenario 2: Protection is not possible
        player = new Player(20, Utility.generateCards());
        player.drawInitialCards();

        // Make sure no protective card is in the hand
        player.getHand().removeIf(card -> card instanceof ProtectCard); // Adjust the condition as needed
        player.getHand().removeIf(card -> card instanceof AttackCard);

        assertFalse(player.checkForProtectionPossibilitiesInHand(lastPlayedCard));
    }

}

