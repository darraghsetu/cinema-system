package models

data class Movie(
    var title: String,
    var director: String,
    var runtime: Int,
    var certification: String,
) {
    internal var movieID = 0

    override fun toString() =
        "(ID: $movieID) " +
        "$title (dir: $director). " +
        "Runtime: " +
        "${
            if(runtime/60 == 0) "" 
            else if(runtime/60 == 1) (runtime/60).toString() + " hour" 
            else (runtime/60).toString() + " hours"
        } " +
        "${
            if(runtime%60 == 0) "" 
            else if(runtime%60 == 1) (runtime%60).toString() + " minute" 
            else (runtime%60).toString() + " minutes"
        }. " +
        "Rated: $certification"
}