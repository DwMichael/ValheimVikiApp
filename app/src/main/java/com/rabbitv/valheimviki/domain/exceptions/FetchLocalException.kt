package com.rabbitv.valheimviki.domain.exceptions

open class FetchLocalException(message: String) : Exception(message)

class CraftingObjectFetchLocalException(message: String) : FetchLocalException(message)