package utils

object CollectionMerger {

	/**
	 * Merge a map on its key with a custom function on this value
	 * (No, i've not written this method !)
	 * List(Map(A->10), Map(B->15, A->7)) => Map(A->17, B->15)
	 */
	def mergeMap[A, B](ms: List[Map[A, B]])(f: (B, B) => B): Map[A, B] =
		(Map[A, B]() /: (for (m <- ms; kv <- m) yield kv)) { (a, kv) =>
    	a + (if (a.contains(kv._1)) kv._1 -> f(a(kv._1), kv._2) else kv)
  }
	
	/**
	 * Merge and count elements in a List
	 * (But I've written this one)
	 * List(A, A, B, A) => Map(A->3, B->1)
	 */
	def mergeAndCountList[A](l: List[A]) = 
		l.foldLeft[Map[A, Int]](Map())((acc, elem) => (
			acc + (if (acc.contains(elem)) (elem -> (acc(elem)+1)) else (elem -> 1))
	))
}