package ostinato.chess.core

import ostinato.core.XY
import org.scalatest._

class GameEndingTest extends FunSpec with Matchers {
  describe("Game ending") {
    it("should find game over but not draw") {
      val game = ChessGame.fromGridString("""........
          |........
          |........
          |........
          |........
          |........
          |.....♛♛.
          |.......♔""".stripMargin).get
      game.isGameOver shouldBe true
      game.isDraw shouldBe false
    }

    it("should find draw and game over") {
      val game = ChessGame.fromGridString("""........
          |........
          |........
          |........
          |........
          |......♛.
          |........
          |.......♔""".stripMargin).get
      game.isDraw shouldBe true
      game.isGameOver shouldBe true
    }

    it("should not find draw nor game over") {
      val game = ChessGame.fromGridString("""........
          |........
          |........
          |........
          |......♛.
          |........
          |........
          |.......♔""".stripMargin).get
      game.isDraw shouldBe false
      game.isGameOver shouldBe false
    }

    it("should not find game over even if threatened") {
      val game = ChessGame.fromGridString("""........
          |........
          |........
          |........
          |........
          |........
          |......♛.
          |.......♔""".stripMargin).get
      game.isGameOver shouldBe false
    }

    it("black should have seven CheckMate actions available") {
      val game = ChessGame
        .fromGridString(
          """........
            |........
            |........
            |........
            |.....♛..
            |......♛.
            |........
            |.......♔""".stripMargin,
          turn = BlackChessPlayer
        )
        .get

      game.blackPlayer.actions(game.board).count {
        case m: ChessAction ⇒ m.isCheckmate
        case _ ⇒ false
      } shouldBe 7
    }

    it("lower queen moving to the right should be a check but not a checkmate") {
      val game = ChessGame
        .fromGridString(
          """........
            |........
            |........
            |........
            |.....♛..
            |......♛.
            |........
            |.......♔""".stripMargin,
          turn = BlackChessPlayer
        )
        .get

      game.board.movementsOfDelta(XY(6, 5), XY(1, 0)) shouldBe
        Set(
          MoveAction(new ♛(XY(6, 5), BlackChessPlayer),
                     XY(1, 0),
                     isCheck = true,
                     isCheckmate = false))
    }

    it("upper queen moving to the right should be a check and a checkmate") {
      val game = ChessGame
        .fromGridString(
          """........
            |........
            |........
            |........
            |.....♛..
            |......♛.
            |........
            |.......♔""".stripMargin,
          turn = BlackChessPlayer
        )
        .get

      game.board.movementsOfDelta(XY(5, 4), XY(2, 0)) shouldBe
        Set(
          MoveAction(new ♛(XY(5, 4), BlackChessPlayer),
                     XY(2, 0),
                     isCheck = true,
                     isCheckmate = true))
    }

    it("upper queen moving to the left should not be a check nor a checkmate") {
      val game = ChessGame
        .fromGridString(
          """........
            |........
            |........
            |........
            |.....♛..
            |......♛.
            |........
            |.......♔""".stripMargin,
          turn = BlackChessPlayer
        )
        .get

      game.board.movementsOfDelta(XY(5, 4), XY(-5, 0)) shouldBe
        Set(
          MoveAction(new ♛(XY(5, 4), BlackChessPlayer),
                     XY(-5, 0),
                     isCheck = false,
                     isCheckmate = false))
    }
  }

  describe("Ending actions") {
    it("draw and resign are always actions for both players") {
      val white = ChessGame.defaultGame.board
      val black = white.doAction(white.nonFinalActions.head).get

      Set(white, black) foreach { b =>
        b.actions.exists {
          case DrawAction(_, _, _) ⇒ true
          case _ ⇒ false
        } shouldBe true
        b.actions.exists {
          case LoseAction(_) ⇒ true
          case _ ⇒ false
        } shouldBe true
      }
    }
  }
}
