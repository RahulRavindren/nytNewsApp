package com.pomeloassignment.android

class NoResultsException(val msg: String = "No results found") : Exception()
class NoInternetException(val msg: String = "No internet available") : Exception()