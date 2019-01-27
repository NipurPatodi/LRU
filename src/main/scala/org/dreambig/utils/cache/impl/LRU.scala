package org.dreambig.utils.cache.impl

import org.dreambig.utils.cache._

import scala.collection.mutable
import scala.util.{Failure, Success, Try}
import org.slf4j.{Logger, LoggerFactory}

class LRU(val size: Int) extends Cache[Int, Int] {

  if (0 == size)
    throw new IllegalArgumentException("Size cannot be zero")


  val logger: Logger = LoggerFactory.getLogger(classOf[LRU])

  private var head: Node = _
  private var tail: Node = _
  private val keyToNodeMap = mutable.Map.empty[Int, Node]

  private case class Node(var key: Int, var value: Int, var next: Node, var prev: Node) {


    override def toString: String = {
      s"[$key $value]"
    }
  }

  private def moveToHead(head: Node, n: Node): Node = {
    n.next = head
    n.prev = null
    head.prev = n
    n
  }

  private def removeFromEnd(tail: Node, keyToNodeMap: mutable.Map[Int, Node]) = {
    if (null != tail) {
      // Remove reference from map
      keyToNodeMap.remove(tail.key)
      val prev = tail.prev
      prev.next = null
      prev
    }
    else {
      logger.info("tail is empty")
      tail
    }
  }

  @Override
  def put(key: Int, value: Int): Boolean = {
    Try {
      val node = Node(key, value, null, null)
      if (null == head && !keyToNodeMap.contains(key)) {
        head = node
        tail = node
        tail.prev = head
      }
      else if (null != head) {
        // if it is already existing key
        if (keyToNodeMap.contains(key)) {
          val curr: Node = keyToNodeMap(key)
          val prev = curr.prev
          val next = curr.next
          curr.key = key
          curr.value = value
          prev.next = next
          next.prev = prev
        }
        // if it max size
        else if (keyToNodeMap.size >= size - 1) {
          tail = removeFromEnd(tail, keyToNodeMap)
        }
        keyToNodeMap(key) = node
        head = moveToHead(head, node)
      }
    } match {
      case Failure(e) =>
        logger.error(s"Exception occured [$e]", e)
        false

      case Success(_) => true
    }

  }


  @Override
  def get(key: Int): Option[Int] = {
    val node = keyToNodeMap.get(key)
    if (node.isDefined) {
      val prev = node.get.prev
      val nxt = node.get.next
      prev.next = nxt
      nxt.prev = prev
      head = moveToHead(head, node.get)
      Option(node.get.value)
    } else {
      logger.info(s"No Value in cache for key $key")
      None
    }

  }

  @Override
  override def toString: String = {
    var node = head
    val sb = new mutable.StringBuilder()
    sb.append("[")
    while (null != node) {
      sb.append(s" ${node.key}=> ${node.value} ")
      node = node.next
    }
    sb.append("]")
    sb.toString
  }

}
