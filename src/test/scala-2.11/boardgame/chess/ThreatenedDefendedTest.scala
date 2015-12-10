package boardgame.chess

import boardgame.chess.core._
import boardgame.core.XY
import org.scalatest.{ShouldMatchers, FunSpec}

class ThreatenedDefendedTest extends FunSpec with ShouldMatchers {
  describe("ChessGame threatened/defended pieces") {
    it("should find no moves for King if it's threatened in every direction") {
      val game = ChessGame.fromString(
        """..♛.....
          |........
          |♛.......
          |...♔....
          |.......♛
          |........
          |........
          |....♛...""".stripMargin)

      val board = game.board

      implicit val rules = game.rules

      movementCount(game, XY(3, 3)) shouldBe 0
    }
    it("should find that the Queen is defended") {
      val game = ChessGame.fromString(
        """..♛.....
          |........
          |..♛.....
          |........
          |........
          |........
          |........
          |........""".stripMargin)
      val board = game.board
      implicit val rules = game.rules

      board.get(XY(2,2)).get.get.isDefended(board) shouldBe true
    }

    it("should find that the Queen is threatened") {
      val game = ChessGame.fromString(
        """..♖.....
          |........
          |..♛.....
          |........
          |........
          |........
          |........
          |........""".stripMargin)
      val board = game.board
      implicit val rules = game.rules

      board.get(XY(2,2)).get.get.isThreatened(board) shouldBe true
    }

    it("should find that the Queen is not threatened") {
      val game = ChessGame.fromString(
        """...♖....
          |........
          |..♛.....
          |........
          |........
          |........
          |........
          |........""".stripMargin)
      val board = game.board
      implicit val rules = game.rules
      val pieces = board.get(XY(2,2)).get.get.owner.pieces(board).toList

      board.get(XY(2,2)).get.get.isThreatened(board) shouldBe false
    }
  }

  private def movementCount(game: ChessGame, point: XY, show: Boolean = false) = {
    val board = game.board
    implicit val rules = game.rules

    val movements = board.get(point).get.get.movements(board)
    if (show) movements map board.move foreach (b => println(b + "\n"))

    movements.size
  }
}