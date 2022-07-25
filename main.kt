package tasklist
import kotlinx.datetime.*

var list = arrayOf<Array<String>>()
var year = "0"
var month = "0"
var day = "0"
var hour = "0"
var minute = "0"
var priority = ""
var count = 0
var taskNum = 0
var taskNumValid = false
val size = 44

fun main() {

   do {
        println("Input an action (add, print, edit, delete, end):")
        var a = readln()
        when(a) {

            "end" -> {
                println("Tasklist exiting!")
                break
            }
            "add" -> {
                askPriority()
                askDate()
                askTime()
                askTaskInput()



            }
            "edit" -> {
                printTask()
                askTaskNum()
                editItem()

            }
            "delete" -> {
                printTask()
                askTaskNum()
                removeItem(taskNum)

            }
            "print" -> printTask()
            else -> {
                println("The input action is invalid")
                continue
            }


        }

    } while (a != "end")


}
fun askPriority() {
    println("Input the task priority (C, H, N, L):")
    val a = readln()
    when {
        a == "C" || a == "c" -> priority = "\u001B[101m \u001B[0m"
        a == "H" || a == "h" -> priority = "\u001B[103m \u001B[0m"
        a == "N" || a == "n" -> priority = "\u001B[102m \u001B[0m"
        a == "L" || a == "l" -> priority = "\u001B[104m \u001B[0m"
        else -> askPriority()
    }
}
fun askDate() {
    println("Input the date (yyyy-mm-dd):")
    val date = readln()
    if (date.contains(Regex("\\d{4}-\\d{1,2}-\\d{1,2}")))
    {
        var (y, m, d) = date.split("-")
        year = y
        month  = m
        day = d
       try{
           val date = LocalDate(year.toInt(), month.toInt(), day.toInt())
       } catch(e: Exception) {
           println("The input date is invalid")
           askDate()
       }


    } else {
        println("The input date is invalid")
        askDate()
    }
}

fun askTime() {
    println("Input the time (hh:mm):")
    val time = readln()
    if (time.contains(Regex("\\d{1,2}:\\d{1,2}")))
    {
        var (hh, mm) = time.split(":")
        hour = hh
        minute = mm
        try {
           val timeInput = LocalDateTime(year.toInt(), month.toInt(), day.toInt(), hour.toInt(), minute.toInt())
        } catch(e: Exception) {
            println("The input time is invalid")
            askTime()
        }

    } else {
        println("The input time is invalid")
        askTime()
    }

}

fun askTaskInput() {
    var b: String
    var array = emptyArray<String>()
    println("Input a new task (enter a blank line to end):")
    do {

        b = readln()
        if (b.isBlank() && array.isEmpty()) {
            println("The task is blank")
            break

        } else if (b.isBlank()) {
            break

        } else if(!b.isBlank() && array.isEmpty()) {
            var dateTime = LocalDateTime(year.toInt(), month.toInt(), day.toInt(), hour.toInt(), minute.toInt())
            val (date, time) = dateTime.toString().split("T")
            val taskDate = LocalDate(year.toInt(), month.toInt(), day.toInt())
            val currentDate = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+2")).date
            val numOfDays = currentDate.daysUntil(taskDate)
            var due = ""
            when {
                numOfDays == 0 -> due = "\u001B[103m \u001B[0m" //T
                numOfDays > 0 -> due = "\u001B[102m \u001B[0m"  //I
                numOfDays < 0 -> due = "\u001B[101m \u001B[0m"  // 0
            }

            array += "$date | $time | $priority | $due"
            array += b.trim()
        } else {
            array += b.trim()
        }

    } while (b.contains(Regex("\\w+")))
    if (array.isNotEmpty()) {
        list += arrayOf(array)
    }
}
fun printTask() {
    if (list.isEmpty()) {
        println("No tasks have been input")
    } else {
        println("""+----+------------+-------+---+---+--------------------------------------------+
| N  |    Date    | Time  | P | D |                   Task                     |""")
        for (i in 0 until list.size) {

            for (j in 0 until list[i].size) {
                if (j == 0) {
                    count++
                    if (count < 10) {
                        println("+----+------------+-------+---+---+--------------------------------------------+")
                        print("| $count  | ${list[i][j]} ")
                    } else {
                        println("+----+------------+-------+---+---+--------------------------------------------+")
                        print("| $count |${list[i][j]} ")

                    }
                } else {
                    if (j == 1) {
                        if (list[i][j].length > 44) {
                            if (list[i][j].length % 44 == 0) {
                                println("|${list[i][j].chunked(size).joinToString("|\n|    |            |       |   |   |")}" + "|")
                            } else {
                                println("|${list[i][j].chunked(size).joinToString("|\n|    |            |       |   |   |")}" + " ".repeat(44 -list[i][j].length % 44) + "|")
                            }
                        } else {
                            val num = 44 - list[i][j].length
                            println("|${list[i][j]}" + " ".repeat(num) + "|")

                        }
                    }else {
                            if (list[i][j].length > 44) {
                                if (list[i][j].length % 44 == 0){
                                    println("|    |            |       |   |   |" + "${list[i][j].chunked(size).joinToString("|\n|    |            |       |   |   |")}" + "|")
                                } else {
                                    println("|    |            |       |   |   |" + "${list[i][j].chunked(size).joinToString("|\n|    |            |       |   |   |")}" + " ".repeat(44 - list[i][j].length % 44) + "|")
                                }
                            } else {
                                val num = 44 - list[i][j].length
                                println("|    |            |       |   |   |" + "${list[i][j]}" + " ".repeat(num) + "|")

                            }
                        }
                    }
                }


            }
            count = 0
            println("+----+------------+-------+---+---+--------------------------------------------+")
        }
    }
fun askTaskNum() {
    if (list.size > 0) {
        println("Input the task number (1-${list.size}):")
        var num = readln()
        if (num.contains(Regex("\\d"))) {
            if (num.toInt() in 1..list.size) {
                taskNum = num.toInt()
                taskNumValid = true
            } else {
                println("Invalid task number")
                taskNumValid = false
                askTaskNum()

            }
        } else {
            println("Invalid task number")
            taskNumValid = false
            askTaskNum()

        }
    }
}
fun removeItem(taskNum: Int){
    if (list.size > 1) {
        val result = list.toMutableList()
        result.removeAt(taskNum - 1)
        list = result.toTypedArray()
        println("The task is deleted")
    } else if(list.size == 1 && taskNum == 1) {
        list = emptyArray()
        println("The task is deleted")
    }
}

fun editItem(){
    if (list.size > 0) {
        println("Input a field to edit (priority, date, time, task):")
        val action = readln()
        when (action) {
            "priority" -> {
                update("priority")
                println("The task is changed")
            }
            "date" -> {
                update("date")
                println("The task is changed")
            }
            "time" -> {
                update("time")
                println("The task is changed")
            }
            "task" -> {
                update("task")
                println("The task is changed")
            }
            else -> {
                println("Invalid field")
                editItem()

            }


        }
    }
}
fun update(update: String){
    var result = list[taskNum - 1][0].split(" | ").toMutableList()
    // "$date $time $priority $due"
    when(update){
        "priority" -> {
            askPriority()
            result[2] = priority
            list[taskNum - 1][0] = result.joinToString(" | ")

        }
        "date" -> {
            askDate()
            try{
                val date = LocalDate(year.toInt(), month.toInt(), day.toInt())
                result[0] = date.toString()
                val currentDate = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+2")).date
                val numOfDays = currentDate.daysUntil(date)
                var due = ""
                when {
                    numOfDays == 0 -> due = "\u001B[103m \u001B[0m" //T
                    numOfDays > 0 -> due = "\u001B[102m \u001B[0m"  //I
                    numOfDays < 0 -> due = "\u001B[101m \u001B[0m"  // 0
                }
                result[3] = due
                list[taskNum - 1][0] = result.joinToString(" | ")

            } catch(e: Exception) {
                println("The input date is invalid")
                askDate()
            }



        }
        "time" -> {
            askTime()

            try {
                val timeInput = LocalDateTime(year.toInt(), month.toInt(), day.toInt(), hour.toInt(), minute.toInt())
                val (date, time) = timeInput.toString().split("T")
                result[1] = time.toString()
                val taskDate = LocalDate(year.toInt(), month.toInt(), day.toInt())
                val currentDate = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+2")).date
                val numOfDays = currentDate.daysUntil(taskDate)
                var due = ""
                when {
                    numOfDays == 0 -> due = "\u001B[103m \u001B[0m" //T
                    numOfDays > 0 -> due = "\u001B[102m \u001B[0m"  //I
                    numOfDays < 0 -> due = "\u001B[101m \u001B[0m"  // 0
                }
                result[3] = due
                list[taskNum - 1][0] = result.joinToString(" | ")

            } catch(e: Exception) {
                println("The input time is invalid")
                askTime()
            }

        }
        "task" -> {
            for (i in 0 until result.size) {
                if (list[taskNum - 1].size > 1) {
                    result.removeLast()
                }
            }

            var b: String
            var array = emptyArray<String>()
            println("Input a new task (enter a blank line to end):")
            do {

                b = readln()
                if (b.isBlank()) {
                    break

                } else {
                    result += b.trim()
                }

            } while (b.contains(Regex("\\w+")))
            list[taskNum - 1][1] = result.joinToString(" | ")



        }
    }



}
