package org.dreambig.utils.cache

trait Cache [K,V] {
  /**
    * Method to put values in catch
    * @param key: key
    * @param value: Value
    * @return true if insertion is success
    */
  def put (key: K,value:V):Boolean

  /***
    * Method to retieve value form cache
    * @param key: Key from which value needs to be accessed
    * @return value if present else None
    */
  def get (key:K):Option[V]
}
