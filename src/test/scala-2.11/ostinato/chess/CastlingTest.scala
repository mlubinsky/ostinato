package ostinato.chess

import ostinato.chess.core._
import org.scalatest.{ ShouldMatchers, FunSpec }
import ostinato.core.XY

class CastlingTest extends FunSpec with ShouldMatchers {
  describe("Castling") {
    it("should determine that black king can castle") {
      val game = ChessGame.fromString(
        """....♚..♜
          |........
          |........
          |........
          |........
          |........
          |........
          |........""".stripMargin, turn = BlackChessPlayer)

      game.board.actions.exists {
        case m: CastlingAction ⇒ true
        case _                 ⇒ false
      } shouldBe true
    }
    it("should determine that black king can't castle because it's white's turn") {
      val game = ChessGame.fromString(
        """....♚..♜
          |........
          |........
          |........
          |........
          |........
          |........
          |........""".stripMargin, turn = WhiteChessPlayer)

      game.board.actions.forall {
        case m: CastlingAction ⇒ false
        case _                 ⇒ true
      } shouldBe true
    }
    it("should determine that black king can't castle because it's not in initial position") {
      val game = ChessGame.fromString(
        """...♚...♜
          |........
          |........
          |........
          |........
          |........
          |........
          |........""".stripMargin, turn = BlackChessPlayer)

      game.board.actions.forall {
        case m: CastlingAction ⇒ false
        case _                 ⇒ true
      } shouldBe true
    }
    it("should determine that black king can't castle because target rook is not in initial position") {
      val game = ChessGame.fromString(
        """....♚.♜.
          |........
          |........
          |........
          |........
          |........
          |........
          |........""".stripMargin, turn = BlackChessPlayer)

      game.board.actions.forall {
        case m: CastlingAction ⇒ false
        case _                 ⇒ true
      } shouldBe true
    }
    it("should determine that black king can't castle because black is on top unless otherwise specified") {
      val game = ChessGame.fromString(
        """........
          |........
          |........
          |........
          |........
          |........
          |........
          |....♚..♜""".stripMargin, turn = BlackChessPlayer)

      game.board.actions.forall {
        case m: CastlingAction ⇒ false
        case _                 ⇒ true
      } shouldBe true
    }
    it("should determine that black king can castle because white is specified to be on the top") {
      implicit val rules = ChessRules.default.copy(whitePawnDirection = 1)
      val game = ChessGame.fromString(
        """........
          |........
          |........
          |........
          |........
          |........
          |........
          |....♚..♜""".stripMargin, turn = BlackChessPlayer)

      game.board.actions.exists {
        case m: CastlingAction ⇒ true
        case _                 ⇒ false
      } shouldBe true
    }
    it("should determine that black king can't castle because the king is threatened") {
      implicit val rules = ChessRules.default.copy(whitePawnDirection = 1)
      val game = ChessGame.fromString(
        """........
          |........
          |........
          |........
          |........
          |........
          |....♖...
          |....♚..♜""".stripMargin, turn = BlackChessPlayer)

      game.board.actions.exists {
        case m: CastlingAction ⇒ true
        case _                 ⇒ false
      } shouldBe false
    }
    it("should determine that black king can't castle because a piece the king will pass through is threatened") {
      implicit val rules = ChessRules.default.copy(whitePawnDirection = 1)
      val game = ChessGame.fromString(
        """........
          |........
          |........
          |........
          |........
          |........
          |.....♖..
          |....♚..♜""".stripMargin, turn = BlackChessPlayer)

      game.board.actions.exists {
        case m: CastlingAction ⇒ true
        case _                 ⇒ false
      } shouldBe false
    }
    it("should determine that black king can't long castle because a piece the king will pass through is threatened") {
      implicit val rules = ChessRules.default.copy(whitePawnDirection = 1)
      val game = ChessGame.fromString(
        """........
          |........
          |........
          |........
          |........
          |........
          |..♖.....
          |♜...♚...""".stripMargin, turn = BlackChessPlayer)

      game.board.actions.exists {
        case m: CastlingAction ⇒ true
        case _                 ⇒ false
      } shouldBe false
    }
    it("should determine that black king can long castle") {
      implicit val rules = ChessRules.default.copy(whitePawnDirection = 1)
      val game = ChessGame.fromString(
        """........
          |........
          |........
          |........
          |........
          |........
          |.....♖..
          |♜...♚...""".stripMargin, turn = BlackChessPlayer)

      game.blackPlayer.kingPiece(game.board).get.actions(game.board)
      game.board.actions.exists {
        case m: CastlingAction ⇒ true
        case _                 ⇒ false
      } shouldBe true
    }
    it("should disable ability for black to castle after castling") {
      val game = ChessGame.fromString(
        """♜...♚...
          |........
          |........
          |........
          |........
          |........
          |........
          |........""".stripMargin, turn = BlackChessPlayer)

      game.board.doAction(
        CastlingAction(♚(XY(4, 0), BlackChessPlayer), XY(-2, 0), ♜(XY(0, 0), BlackChessPlayer), XY(3, 0))
      ).get.castlingAvailable shouldBe Map(
          (WhiteChessPlayer, CastlingSide.Queenside) -> true,
          (WhiteChessPlayer, CastlingSide.Kingside) -> true,
          (BlackChessPlayer, CastlingSide.Queenside) -> false,
          (BlackChessPlayer, CastlingSide.Kingside) -> false
        )
    }
    it("should disable ability for white to castle after castling") {
      val game = ChessGame.fromString(
        """........
          |........
          |........
          |........
          |........
          |........
          |........
          |♖...♔...""".stripMargin, turn = WhiteChessPlayer)

      game.board.doAction(
        CastlingAction(♚(XY(4, 7), WhiteChessPlayer), XY(-2, 0), ♜(XY(0, 7), WhiteChessPlayer), XY(3, 0))
      ).get.castlingAvailable shouldBe Map(
          (WhiteChessPlayer, CastlingSide.Queenside) -> false,
          (WhiteChessPlayer, CastlingSide.Kingside) -> false,
          (BlackChessPlayer, CastlingSide.Queenside) -> true,
          (BlackChessPlayer, CastlingSide.Kingside) -> true
        )
    }
  }
}
