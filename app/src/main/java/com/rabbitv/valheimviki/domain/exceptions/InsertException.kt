package com.rabbitv.valheimviki.domain.exceptions

open class InsertException(message: String) : Exception(message)

class WorkerInsertException(message: String) : InsertException(message)
class CreaturesInsertException(message: String) : InsertException(message)
class BiomesInsertException(message: String) : InsertException(message)
class RelationsInsertException(message: String) : InsertException(message)
