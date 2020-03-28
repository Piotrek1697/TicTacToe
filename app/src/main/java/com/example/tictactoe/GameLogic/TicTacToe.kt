class TicTacToe() {
    private var gameArray = arrayOf<Array<GameValues>>()
    private var players = arrayOf(Player("First", GameValues.X), Player("Second", GameValues.O))
    var winner: Winner = Winner(GameValues.EMPTY, false)
    private var playWithCPU : Boolean = false
    private var bool: Boolean = false
    var isGameEnd : Boolean = false
    var currentPlayer = players[bool.toInt()]
    init {
        this.gameArray = Array(3) { Array(3) { GameValues.EMPTY } }
    }

    fun reset(){
        gameArray = Array(3) { Array(3) { GameValues.EMPTY } }
        players = arrayOf(Player("First", GameValues.X), Player("Second", GameValues.O))
        winner = Winner(GameValues.EMPTY, false)
        playWithCPU = false
        bool = false
        isGameEnd = false
    }

    private fun playWithComputer(result: String?): Boolean {
        if (result.isNullOrEmpty())
            return false
        else if (result.length == 1 && result[0].toUpperCase() == 'C') {
            return true
        }
        return false
    }

    /**
     * Function to play Tic Tac Toe.
     * Player has to choose if he wanted to play with computer or with other player.
     * Input value must be consistent with pattern (ex. 11X) where first number is row number,
     * second number is column number and last character is game value, X or O.
     * After put game value algorithm, that check if someone won, is started.
     */

    fun playGame(playerCords : String?){
        currentPlayer = players[bool.toInt()]
        var cords : String? = playerCords
        if (cords != null) {
            val move = PlayerMove.getMove(cords)
            if (currentPlayer.gameValue == GameValues.EMPTY && players[(!bool).toInt()].gameValue != move.gameValue)
                currentPlayer.gameValue = move.gameValue
            if (move.gameValue != currentPlayer.gameValue)
                println("Game value must be same as in first turn")
            else {
                val proper = makeMove(cords)
                if (proper) {
                    winner = checkWin(cords)
                    if (winner.win || !isAnyEmpty()){
                        isGameEnd = true
                    }else
                        bool = !bool
                } else
                    println("Please put value in non empty slot")
            }
        }
        currentPlayer = players[bool.toInt()]
    }

    fun initializeTicTac(playWithCPU : Boolean, firstPlayerName : String?, secondPlayerName : String?){
        reset()
        this.playWithCPU = playWithCPU
        if (this.playWithCPU) {
            players[0].name = firstPlayerName!!
            players[1].name = "Computer"
        }
        else{
            players[0].name = firstPlayerName!!
            players[1].name = secondPlayerName!!
        }
        currentPlayer = players[0]
    }


    fun drawCords(): String? {
        return "${(1..3).random()}${(1..3).random()}${GameValues.O}"
    }

    private fun isEmpty(playerMove: PlayerMove): Boolean {
        return this.gameArray[playerMove.position.row - 1][playerMove.position.column - 1] == GameValues.EMPTY
    }

    private fun isAnyEmpty(): Boolean {
        return gameArray.any { it.any { it1 -> it1 == GameValues.EMPTY } }
    }

    private fun makeMove(moveString: String): Boolean {
        val playerMove = PlayerMove.getMove(moveString)
        if (isEmpty(playerMove)) {
            this.gameArray[playerMove.position.row - 1][playerMove.position.column - 1] = playerMove.gameValue
            return true
        }
        return false
    }

    /**
     * This function was created to check if player wins.
     * Input: moveString - patter to put game values (X or O)
     * Algorithm check nearby position of input, for example if input is like 11X,
     * algorithm check if indexes of (21,22,12) contains X, if one of index contains X
     * check next value of the same direction
     */
    private fun checkWin(moveString: String): Winner {
        val playerMove = PlayerMove.getMove(moveString)
        val position = playerMove.position
        val nearbyPositions = nearbyPositions(position)
        var counter = 0
        var directionArray: MutableList<ValueWithDirection> = mutableListOf()
        for (pos in nearbyPositions) {
            if (gameArray[pos.row - 1][pos.column - 1] == playerMove.gameValue) {
                counter = 2
                val direction = checkDirection(position, pos)
                var newPos: Position

                newPos = when (direction) {
                    Direction.SOUTH_EAST -> Position(pos.row + 1, pos.column + 1)
                    Direction.NORTH -> Position(pos.row - 1, pos.column)
                    Direction.SOUTH -> Position(pos.row + 1, pos.column)
                    Direction.WEST -> Position(pos.row, pos.column - 1)
                    Direction.EAST -> Position(pos.row, pos.column + 1)
                    Direction.NORTH_WEST -> Position(pos.row - 1, pos.column - 1)
                    Direction.NORTH_EAST -> Position(pos.row - 1, pos.column + 1)
                    Direction.SOUTH_WEST -> Position(pos.row + 1, pos.column - 1)
                }

                if (newPos.checkBounds()) {
                    if (gameArray[newPos.row - 1][newPos.column - 1] == playerMove.gameValue)
                        counter++
                }
                directionArray.add(ValueWithDirection(direction, playerMove.gameValue, counter))
            }
        }
        return Winner(playerMove.gameValue, checkDirectionArray(directionArray))
    }

    private fun checkDirectionArray(directionArray: MutableList<ValueWithDirection>): Boolean {
        if (directionArray.find { it.valueCounter == 3 } == null) {
            return (directionArray.find { it.direction == Direction.SOUTH_EAST } != null &&
                    directionArray.find { it.direction == Direction.NORTH_WEST } != null) ||
                    (directionArray.find { it.direction == Direction.SOUTH_WEST } != null &&
                            directionArray.find { it.direction == Direction.NORTH_EAST } != null) ||
                    (directionArray.find { it.direction == Direction.EAST } != null &&
                            directionArray.find { it.direction == Direction.WEST } != null) ||
                    (directionArray.find { it.direction == Direction.NORTH } != null &&
                            directionArray.find { it.direction == Direction.SOUTH } != null)
        } else
            return true
    }

    private fun checkDirection(first: Position, second: Position): Direction {
        if ((second.column < first.column) && (second.row > first.row))
            return Direction.SOUTH_WEST
        else if ((second.column < first.column) && (second.row < first.row))
            return Direction.NORTH_WEST
        else if ((second.column > first.column) && (second.row < first.row))
            return Direction.NORTH_EAST
        else if ((second.column > first.column) && (second.row > first.row))
            return Direction.SOUTH_EAST
        else if (second.column > first.column)
            return Direction.EAST
        else if (second.column < first.column)
            return Direction.WEST
        else if (second.row > first.row)
            return Direction.SOUTH
        else
            return Direction.NORTH
    }

    private fun nearbyPositions(position: Position): MutableList<Position> {
        var positionList: MutableList<Position> = mutableListOf()
        for (i in -1..1) {
            for (j in -1..1) {
                if (!(i == 0 && j == 0))
                    positionList.add(Position(i, j))
            }
        }
        positionList.forEach {
            it.column += position.column
            it.row += position.row
        }
        return checkBounds(positionList)
    }

    private fun checkBounds(list: MutableList<Position>): MutableList<Position> {
        return list.filterIndexed { ix, element ->
            element.column in 1..3 && element.row in 1..3
        }.toMutableList()
    }

    fun getArray(): Array<Array<GameValues>> {
        return this.gameArray
    }


    object PlayerMove {
        var position: Position = Position(0, 0)
        var gameValue: GameValues = GameValues.EMPTY

        fun getMove(moveString: String): PlayerMove {
            this.position.row = moveString[0].toString().toInt()
            this.position.column = moveString[1].toString().toInt()
            this.gameValue = GameValues.valueOf(moveString[2].toString().toUpperCase())

            return this
        }
    }

    fun Boolean.toInt(): Int {
        return if (this) 1 else 0
    }

    fun printArray(array: Array<Array<GameValues>>) {
        for (el in array) {
            for (el2 in el) {
                print("${el2.toString()[0]} ")
            }
            println()
        }
    }

}