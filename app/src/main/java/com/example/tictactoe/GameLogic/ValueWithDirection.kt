class ValueWithDirection {

    var direction : Direction = Direction.NORTH
    var gameValue : GameValues = GameValues.EMPTY
    var valueCounter : Int = 0

    constructor(direction: Direction, gameValue: GameValues, valueCounter: Int) {
        this.direction = direction
        this.gameValue = gameValue
        this. valueCounter = valueCounter
    }

    override fun toString(): String {
        return "ValueWithDirection(direction=$direction, gameValue=$gameValue, valueCounter=$valueCounter)"
    }


}