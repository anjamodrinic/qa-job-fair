package test;

import cards.AttackCard;
import cards.Card;
import game.Game;
import org.junit.Assert;
import org.junit.Test;
import player.Player;
import utility.Utility;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestGame {
    @Test
    public void testEmptyDeck(){
        new Utility();

        List<Card> emptyDeck = new ArrayList<>();

        Player player1 = new Player(20, emptyDeck);
        Player player2 = new Player(20, Utility.generateCards());

        Game game = new Game(player1, player2);
        game.startGame();

        assertTrue(game.getGameEnded());
    }

    @Test
    public void testGameEnd(){
        new Utility();
        List<Card> emptyDeck = new ArrayList<>();
        Player player1 = new Player(20, emptyDeck);
        Player player2 = new Player(20, Utility.generateCards());

        Game game = new Game(player1, player2);
        assertTrue(game.testCommandIsPlayerWithoutOptionsToPlay());
    }

    @Test
    public void testPlayAttackCard() {
        Player player1 = new Player(20, Utility.generateCards());
        Player player2 = new Player(20, Utility.generateCards());
        Game game = new Game(player1, player2);

        // Add an AttackCard to player1's hand
        AttackCard attackCard = new AttackCard(5); // Assuming '5' is the attack value
        player1.getHand().add(attackCard);

        // Before playing the card
        int initialAttackDamage = player1.getDamage();
        boolean initialTurnStatus = game.testCommandPlayTurn(player1,player2); // Assuming isPlayerTurn() checks if it's player1's turn


        player1.playCard(attackCard.getNumber());

        // After playing the card
        int newAttackDamage = player1.getDamage();
        boolean newTurnStatus = game.testCommandPlayTurn(player1,player2);

        // Check if attack damage increased
        assertTrue(newAttackDamage > initialAttackDamage);

        // Check if the turn has ended
        assertFalse(newTurnStatus);
    }

}
