package reg.util

import chisel3._
import chisel3.util._
import chisel3.iotesters._
import org.scalatest.{FlatSpec, Matchers}
import scala.util.Random
import scala.collection.mutable.Queue

class FIFOTester(val c: FIFO) extends PeekPokeTester(c) {
  val q: Queue[Int] = new Queue[Int]()
  reset(1)
  expect(c.io.empty, true)

  // Pass 1: Insertion test
  poke(c.io.en_out, false)
  poke(c.io.en_in, true)
  for (n <- 0 until 7) {
    val x = Random.nextInt(16)
    q.enqueue(x)
    poke(c.io.in, x)
    step(1)
    expect(c.io.out, 0)
  }
  expect(c.io.empty, false)
  expect(c.io.full, true)

  // Pass 2: Continuing insertion (should reject)
  for (n <- 0 until 7) {
    val x = Random.nextInt(16)
    poke(c.io.in, x)
    step(1)
    expect(c.io.full, true)
    expect(c.io.out, 0)
  }
  poke(c.io.en_in, false)

  // Pass 3: Out test
  poke(c.io.en_out, true)
  for (n <- 0 until 4) {
    step(1)
    expect(c.io.out, q.dequeue)
    expect(c.io.full, false)
  }

  // Pass 4: Simultaneous I/O
  poke(c.io.en_out, true)
  poke(c.io.en_in, true)
  for (n <- 0 until 16) {
    val x = Random.nextInt(16)
    q.enqueue(x)
    poke(c.io.in, x)
    step(1)
    expect(c.io.out, q.dequeue)
    expect(c.io.empty, false)
    expect(c.io.full, false)
  }
}
