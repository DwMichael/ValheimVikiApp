package com.rabbitv.valheimviki.domain.exceptions

open class FetchLocalException(message: String) :Exception(message)

class BossesFetchLocalException(message: String) : FetchLocalException(message)
class MiniBossesFetchLocalException(message: String) : FetchLocalException(message)
class CreaturesFetchLocalException(message: String) : FetchLocalException(message)
class RelationsFetchLocalException(message: String) : FetchLocalException(message)
class BiomesFetchLocalException(message: String) : FetchLocalException(message)