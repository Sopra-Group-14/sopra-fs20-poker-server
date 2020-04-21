package ch.uzh.ifi.seal.soprafs20.cards;

import ch.uzh.ifi.seal.soprafs20.entity_in_game.Player;

import java.util.LinkedList;
import java.util.List;

public class WinnerCalculator {

        //Auxiliary tuple type to avoid code repetition
        private class PlayerHandTuple{

            private List<Player> playerList;
            private List<PokerHand> handList;

            PlayerHandTuple(List<Player> playerList, List<PokerHand> handList){
                this.playerList = playerList;
                this.handList = handList;
            }

            private List<Player> getPlayerList(){
                return playerList;
            }

            private List<PokerHand> getHandList(){
                return handList;
            }

        }

        //Recursive method to compare all the valid attributes
        private PlayerHandTuple compareAttributes(PlayerHandTuple playerHandTuple, int currentAttribute){

            //This is the abort-statement; if we have compared all attributes, the next attribute wouldn't be valid or there is only 1 winner left
            if(currentAttribute > 4 || playerHandTuple.getHandList().get(0).getRankList().get(currentAttribute) == null || playerHandTuple.getPlayerList().size() == 1){
                return playerHandTuple;
            }

            int i;
            List<Player> winningPlayers = playerHandTuple.getPlayerList();
            List<PokerHand> winningHands = playerHandTuple.getHandList();
            List<PokerHand> newWinningHands = new LinkedList<>();
            List<Player> newWinningPlayers = new LinkedList<>();

            newWinningHands.add(winningHands.get(0));
            newWinningPlayers.add(winningPlayers.get(0));
            for(i=1;i<winningHands.size();i++){
                if(cardAnalyser.rankToInt(winningHands.get(i).getRankList().get(currentAttribute)) > cardAnalyser.rankToInt(newWinningHands.get(0).getRankList().get(currentAttribute))){
                    while(newWinningHands.size()>0){
                        newWinningHands.remove(0);
                        newWinningPlayers.remove(0);
                    }
                    newWinningHands.add(winningHands.get(i));
                    newWinningPlayers.add(winningPlayers.get(i));
                }else if(cardAnalyser.rankToInt(winningHands.get(i).getRankList().get(currentAttribute)) == cardAnalyser.rankToInt(newWinningHands.get(0).getRankList().get(currentAttribute))){
                    newWinningHands.add(winningHands.get(i));
                    newWinningPlayers.add(winningPlayers.get(i));
                }
            }

            return  compareAttributes(new PlayerHandTuple(newWinningPlayers, newWinningHands), currentAttribute + 1);

        }

        private CardAnalyser cardAnalyser = new CardAnalyser();

        public List<Player> isWinner(List<Player> players, List<Card> tableCards){

            List<PokerHand> hands = new LinkedList<>();
            List<PokerHand> winningHands = new LinkedList<>();
            List<Player> winningPlayers = new LinkedList<>();

            int i,e;

            //Make new list of aggregate player hands; their hand together with table cards
            List<List<Card>> aggregateHands = new LinkedList<>();
            for(i=0;i<players.size();i++){
                aggregateHands.add(new LinkedList<>());
                for(e=0;e<2;e++){
                    aggregateHands.get(i).add(players.get(i).getHand().get(e));
                }
                for(e=0;e<3;e++){
                    aggregateHands.get(i).add(tableCards.get(e));
                }
            }

            //Get the base winning player list; only looking at the base hand combination value
            for(i=0;i<players.size();i++) {
                PokerHand currentHand = cardAnalyser.getPokerHand(aggregateHands.get(i));
                hands.add(currentHand);
                if (winningHands.size() == 0) {
                    winningHands.add(currentHand);
                    winningPlayers.add(players.get(i));
                }else if(currentHand.getComboValue() > winningHands.get(0).getComboValue()) {
                    while (0 < winningHands.size()) {
                        winningHands.remove(0);
                        winningPlayers.remove(0);
                    }
                    winningHands.add(currentHand);
                    winningPlayers.add(players.get(i));
                }else if(currentHand.getComboValue() == winningHands.get(0).getComboValue()){
                    winningHands.add(currentHand);
                    winningPlayers.add(players.get(i));
                }
            }

            //If there is more than 1 player with the same base hand combination value, we check the secondary etc. conditions
            if(winningHands.size() > 1){

                return compareAttributes(new PlayerHandTuple(winningPlayers, winningHands), 0).getPlayerList();

            }

            return winningPlayers;
        }

}
