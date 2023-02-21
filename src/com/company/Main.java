package com.company;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
  int playerFirePower = 2500;
  int playerSoldiers = 25;
  int playerField = -10;
  int computerFirePower = 2500;
  int computerSoldiers = 25;
  int computerField = 10;

  String red = "\u001B[31m";
  String colorReset = "\u001B[0m";
  String green = "\u001B[32m";


  public void runGame() {
    //Single responsibility mangler her.
    //Denne klasse kunne opnå single responsibility hvis den blev opdelt i flere klasser.
    //Liskov substitution: Med opsætning af player class kunne man lave subclasses for forskellige typer players fx. med forskellige sværhedsgrader
    //Evt nedarvning for computer player fra player class
    //Der er ingen interfaces.
    //
    System.out.println("Velkommen til Line Battle");
    System.out.println("Du spiller imod computeren. Vinderen er den der først dræber alle modstanderens 25 soldater.");
    System.out.println("Du kan kun angribe hvis du har ammunition. Du starter med 2500, men kan få mere ved at rykke baglæns for at hente mere.");
    System.out.println("Spillepladen strækker sig fra -10 til 10. Du starter på -10 og din modstander på 10.");
    System.out.println("I kan først ramme hinanden når i er fem felter væk. Din spejder kan spotte fjenden op til tre felter væk.");
    System.out.println("Spillet starter nu.\n\n");
    playerTurn();
  }

  public void playerTurn() {
    System.out.println(green + "Hvordan vil du handle?");
    System.out.println("1. Ryk frem");
    System.out.println("2. Ryk tilbage");
    System.out.println("3. Angrib" + colorReset);
    Scanner playerChoice = new Scanner(System.in);
    int playerAction = playerChoice.nextInt();
    if (playerAction == 1) {
      playerMoveForward();
    } else if (playerAction == 2) {
      playerMoveBackwards();
    } else if (playerAction == 3) {
      playerAttack();
    } else {
      System.out.println(green +"Dette er ikke en gyldig valgmulighed. Vælg igen." + colorReset);
      playerTurn();
    }
  }


  public void computerTurn() {
    int computerDiceThrow = dice();
    if (computerDiceThrow == 1 || computerDiceThrow == 2) {
      computerMoveForward();
    } else if (computerDiceThrow == 3 || computerDiceThrow == 4) {
      computerMoveBackwards();
    } else {
      computerAttack();
    }

  }


  public int dice() {
    int dice = ThreadLocalRandom.current().nextInt(1, 7);
    return dice;
  }


  public void playerMoveForward() {

    if (playerField == 10) {
      System.out.println(green + "Du kan ikke rykke længere fremad. Vælg en anden handling." + colorReset);
      playerTurn();
    }

    int movement;
    int moveThrow = dice();
    if (moveThrow <= 3) {
      movement = 1;
    } else {
      movement = 2;
    }
    playerField += movement;

    if (playerField >= 10) {
      System.out.println(green + "Du er noget til sidst felt, du kan ikke rykke længere fremad." + colorReset);
      playerField = 10;
    }
    int fieldsAway = Math.abs(computerField - playerField);

    if (fieldsAway <= 3) {
      System.out.println(green + "Din spejder har spottet fjenden. De er " + fieldsAway + " felter væk, og står på felt" + computerField + colorReset);
    }
    gameStatusPlayer();
  }

  public void computerMoveForward() {

    if (computerField == -10) {
      computerMoveBackwards();
    }
    System.out.println(red + "Fjenden marcherer fremad" + colorReset);

    int movement;
    int moveThrow = dice();
    if (moveThrow <= 3) {
      movement = -1;
    } else {
      movement = -2;
    }

    computerField += movement;

    if (computerField < -10) {
      computerField = -10;
    }

    int fieldsAway = Math.abs(computerField - playerField);
    if (fieldsAway <= 3) {
      System.out.println(green + "Din spejder har spottet fjenden. De er " + fieldsAway + " felter væk, og står på felt  " + computerField + colorReset);
    }
    gameStatusComputer();
  }

  public void playerMoveBackwards() {

    if (playerField == -10) {
      System.out.println("Du kan ikke rykke længere bagud. Vælg en anden handling.");
      playerTurn();
    }

    int movement;
    int moveThrow = dice();
    if (moveThrow <= 3) {
      movement = -1;
    } else {
      movement = -2;
    }


    playerFirePower += 250;
    playerField += movement;

    if (playerField <= -10) {
      playerField = -10;
    }
    int fieldsAway = Math.abs(computerField - playerField);

    if (fieldsAway <= 3) {
      System.out.println(green + "Din spejder har spottet fjenden. De er " + fieldsAway + " felter væk" + colorReset);
    }
    gameStatusPlayer();
  }

  public void computerMoveBackwards() {

    if (computerField == 10) {
      computerMoveForward();
    }

    System.out.println(red + "Fjenden laver taktisk tilbagetrækning" + colorReset);

    int movement;
    int moveThrow = dice();
    if (moveThrow <= 3) {
      movement = 1;
    } else {
      movement = 2;
    }
    computerFirePower += 250;
    computerField += movement;

    if (computerField <= 10) {
      computerField = 10;
    }

    int fieldsAway = Math.abs(computerField - playerField);
    if (fieldsAway <= 3) {
      System.out.println(green + "Din spejder har spottet fjenden. De er " + fieldsAway + " felter væk" + colorReset);
    }
    gameStatusComputer();
  }

  public void playerAttack() {
    int fieldsAway = Math.abs(computerField - playerField);

    if (playerFirePower < 100) {
      System.out.println(green + "Du har ikke tilstrækkelig ammunition til et angreb. Vælg en anden handling.");
      playerTurn();
    }
    int attackThrow = dice();
    int firePowerDamage = attackThrow * 100;

    if (playerFirePower < firePowerDamage) {
      System.out.println(green + "Angrebet krævede mere firepower end du havde tilgængelig, og blev derfor ikke udført.");
      System.out.println("Prøv igen næste runde, og lav evt. træk bagud for at opbygge  mere ammunition.");
      System.out.println("Din tur afsluttes." + colorReset);
      gameStatusPlayer();
    }

    playerFirePower -= firePowerDamage;

    switch (fieldsAway) {
      case 0 -> {
        computerSoldiers -= 6;
        System.out.println(green + "Du har dræbt 6 fjendtlige soldater!" + colorReset);
      }
      case 1 -> {
        computerSoldiers -= 5;
        System.out.println(green + "Du har dræbt 5 fjendtlige soldater!" + colorReset);
      }
      case 2 -> {
        computerSoldiers -= 4;
        System.out.println(green + "Du har dræbt 4 fjendtlige soldater!" + colorReset);
      }
      case 3 -> {
        computerSoldiers -= 3;
        System.out.println(green + "Du har dræbt 3 fjendtlige soldater!" + colorReset);
      }
      case 4 -> {
        computerSoldiers -= 2;
        System.out.println(green + "Du har dræbt 2 fjendtlige soldater!" + colorReset);
      }
      case 5 -> {
        computerSoldiers -=1;
        System.out.println(green + "Du har dræbt 1 fjendtlige soldater!" + colorReset);
      }
      default -> System.out.println(green + "Fjenden er udenfor rækkevidde" + colorReset);
    }

    gameStatusPlayer();
  }


  public void computerAttack() {



    if (computerFirePower < 100) {
      computerTurn();
    }

    System.out.println(red + "Du bliver angrebet!" + colorReset);

    int attackThrow = dice();

    int firePowerDamage = attackThrow * 100;

    if (computerFirePower < firePowerDamage) {
      System.out.println(red + "Computeren havde ikke tilstrækkelig ammunition til at udføre angrebet" + colorReset);
      gameStatusPlayer();
    }

    computerFirePower -= firePowerDamage;

    int fieldsAway = Math.abs(computerField - playerField);

    switch (fieldsAway) {
      case 0 -> {
        System.out.println(red + "Du har mistet 6 soldater!");
        playerSoldiers -= 6;
      }
      case 1 -> {
        System.out.println("Du har mistet 5 soldater!");
        playerSoldiers -= 5;
      }
      case 2 -> {
        System.out.println("Du har mistet 4 soldater!");
        playerSoldiers -= 4;
      }
      case 3 -> {
        System.out.println("Du har mistet 3 soldater!");
        playerSoldiers -= 3;
      }
      case 4 -> {
        System.out.println("Du har mistet 2 soldater!");
        playerSoldiers -= 2;
      }
      case 5 -> {
        System.out.println("Du har mistet 1 soldat!");
        playerSoldiers -= 1;
      }
      case 6 -> {
        System.out.println("Du er udenfor rækkevidde" + colorReset);
      }
    }
      gameStatusComputer();
    }

  public void gameStatusPlayer() {
    System.out.println("\n" + green + "Din resterende ammunition er: " + playerFirePower);
    System.out.println("Spillers antal soldater: " + playerSoldiers);
    System.out.println("Du står på felt: " + playerField + colorReset + "\n");
    System.out.println(red + "Computers antal soldater: " + computerSoldiers + "\n" + colorReset);

    Scanner playerChoice = new Scanner(System.in);
    System.out.println(green + "Indtast vilkårligt tegn efterfulgt af enter for at afslutte din tur." + colorReset);
    playerChoice.next();


    if (computerSoldiers <= 0) {
      endGame();
    } else {
      computerTurn();
    }
  }

  public void gameStatusComputer() {
    System.out.println("\n" + green + "Spillers antal soldater: " + playerSoldiers + colorReset);
    System.out.println(red + "Computers antal soldater: " + computerSoldiers + "\n");
    System.out.println("Computerens tur er slut.\n\n" + colorReset);

    if (playerSoldiers <= 0) {
      endGame();
    } else {
      playerTurn();
    }
  }

  public void endGame() {
    if (playerSoldiers > computerSoldiers) {
      System.out.println(green + "Tillykke, du har vundet krigen. Spillet er slut." + colorReset);
    } else {
      System.out.println(red + "Fjenden var dig overlegen, og du har tabt spillet." + colorReset);
    }
  }


  public static void main(String[] args) {
    Main obj = new Main();

    obj.runGame();

  }
}
