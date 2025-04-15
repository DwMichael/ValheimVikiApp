package com.rabbitv.valheimviki.domain.exceptions

open class FetchAndInsertException(message: String) : Exception(message)

class CreatureFetchAndInsertException(message: String) : FetchAndInsertException(message)
class RelationFetchAndInsertException(message: String) : FetchAndInsertException(message)