package francisco.simon.navcomponent.model

class LoadDataException : Exception()

class DuplicateException(
    val duplicatedValue: String,
) : Exception("The list can't contain duplicated items")