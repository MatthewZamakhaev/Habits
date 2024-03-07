package app.doubletapp.habits.recyclerview

data class Habit(
    var id: Int = 0,
    var name: String,
    var description: String,
    var priority: String,
    var type: String,
    var frequency: String,
    var colorR: String,
    var colorG: String,
    var colorB: String,
    var textColor: String
)
