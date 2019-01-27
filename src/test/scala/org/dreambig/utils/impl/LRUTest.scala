package org.dreambig.utils.impl

import org.dreambig.utils.cache.Cache
import org.dreambig.utils.cache.impl.LRU
import org.scalatest.FlatSpec


class LRUTest extends FlatSpec{


  "A LRU" should " store value in order of insertion" in {
    val cache:Cache[Int,Int] = new LRU(3)
    cache.put(1,100)
    cache.put(2,200)
    cache.put(3,300)
    assert(cache.toString=="[ 3=> 300  2=> 200  1=> 100 ]")

  }

  "A LRU" should " store value in order of retrieval" in {
    val cache:Cache[Int,Int] = new LRU(3)
    cache.put(1,100)
    cache.put(2,200)
    cache.put(3,300)
    cache.get(2)
    assert(cache.toString=="[ 2=> 200  3=> 300  1=> 100 ]")
  }

  "A LRU" should " give nothing if size is zero" in {
    assertThrows[IllegalArgumentException] {
      val cache = new LRU(0)
    }
  }

  "A LRU" should " respect size " in {
    val cache:Cache[Int,Int] = new LRU(3)
    cache.put(1,100)
    cache.put(2,200)
    cache.put(3,300)
    cache.put(4,400)
    cache.put(5,500)
    assert(cache.toString=="[ 5=> 500  4=> 400  3=> 300 ]")
  }


}
