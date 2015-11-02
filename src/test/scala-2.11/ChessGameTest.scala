import org.scalatest.{ShouldMatchers, FunSpec}

class ChessGameTest extends FunSpec with ShouldMatchers {
  describe("ChessGame") {
    it("should print out a default starting game of Chess") {
      ChessGame.defaultGame.board.toString shouldBe
        """♜♞♝♛♚♝♞♜
          |........
          |........
          |........
          |........
          |........
          |........
          |♖♘♗♕♔♗♘♖""".stripMargin
    }

    it("should find 14 possible movements for a Rook") {
      val game = ChessGame.fromString(
        """........
          |........
          |........
          |...♜....
          |........
          |........
          |........
          |........""".stripMargin)

      val board = game.board
      implicit val rules = game.rules

      val movements = game.board.get(3,3).get.get.movements(board)
      movements map board.move foreach (b => println(b + "\n"))

      movements.size shouldBe 14
    }

    it("should find 0 possible movements for a Rook") {
      val game = ChessGame.fromString(
        """........
          |........
          |...♜....
          |..♜♜♜...
          |...♜....
          |........
          |........
          |........""".stripMargin)

      val board = game.board
      implicit val rules = game.rules

      val movements = game.board.get(3,3).get.get.movements(board)
      movements map board.move foreach (b => println(b + "\n"))

      movements.size shouldBe 0
    }

    it("should find 1 possible movements for a Rook") {
      val game = ChessGame.fromString(
        """........
          |........
          |...♜....
          |..♜♜♜...
          |...♖....
          |........
          |........
          |........""".stripMargin)

      val board = game.board
      implicit val rules = game.rules

      val movements = game.board.get(3,3).get.get.movements(board)
      movements map board.move foreach (b => println(b + "\n"))

      movements.size shouldBe 1
    }
  }


}
