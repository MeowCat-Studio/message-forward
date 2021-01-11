package io.github.itsusinn.extension

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


private val loggerCache = HashMap<Any, Logger>()


//   implementation("org.slf4j:slf4j-api:1.7.30")
//   implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.14.0")
//   implementation("org.apache.logging.log4j:log4j-core:2.14.0")
//   implementation("org.apache.logging.log4j:log4j-api:2.14.0")

val Any.logger:Logger
   get() =
      loggerCache.getOrPut(this.javaClass){ LoggerFactory.getLogger(this.javaClass) }