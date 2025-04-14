package com.rabbitv.valheimviki.domain.exceptions

open class FetchException(message: String) : Exception(message)

class MaterialFetchException(message: String) : FetchException(message)
class WorkerFetchException(message: String) : FetchException(message)
class CreatureFetchException(message: String) : FetchException(message)
class BiomeFetchException(message: String) : FetchException(message)
class RelationFetchException(message: String) : FetchException(message)
