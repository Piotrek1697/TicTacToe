class Position(row : Int, column : Int) {
    var row : Int = 0
    var column : Int = 0

    init {
        this.row = row
        this.column = column
    }

    public fun checkBounds(): Boolean {
        return row in 1..3 && column in 1..3
    }

    override fun toString(): String {
        return "Row: $row, Column: $column"
    }

}