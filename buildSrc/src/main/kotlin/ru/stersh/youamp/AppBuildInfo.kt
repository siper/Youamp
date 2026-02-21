package ru.stersh.youamp

object AppBuildInfo {
    const val VERSION_NAME = "2.1.3"
    const val VERSION_CODE = 34
    const val PACKAGE_NAME = "ru.stersh.youamp"

    val CLEAR_VERSION_NAME = clearVersionName()

    private fun clearVersionName(): String {
        val index = VERSION_NAME.indexOf('-')
        return if (index == -1) {
            VERSION_NAME
        } else {
            VERSION_NAME.substring(
                0,
                index,
            )
        }
    }
}
