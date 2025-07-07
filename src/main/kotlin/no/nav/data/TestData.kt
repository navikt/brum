package no.nav.data

data class TestData(val Gjennomforingsgruppe: Int, val Landegruppe3: Int, val SBestemt: Int, val STilpasset: Int)

fun getTestData1(): Array<TestData> {
    return arrayOf(
        TestData(0, 0, 2, 3),
        TestData(1, 373, 363, 390),
        TestData(2, 358, 370, 397),
        TestData(3, 346, 417, 372),
        TestData(4, 386, 373, 351),
        TestData(5, 391, 348, 355),
        TestData(6, 354, 368, 389),
        TestData(7, 330, 364, 401),
        TestData(8, 347, 388, 341),
        TestData(9, 401, 360, 362)
    )
}

fun getTestData2(): Array<TestData> {
    return arrayOf(
        TestData(0, 0, 2, 3),
        TestData(1, 6, 0, 0),
        TestData(2, 4, 7, 7),
        TestData(3, 1, 1, 1)
    )
}










