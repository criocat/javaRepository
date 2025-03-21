(defn leftHold [f] (fn [& arr]
                     (reduce f arr)))
(defn vect? [a] (and
                    (= (type a) clojure.lang.PersistentVector)
                    (every? #(number? %) a)))

(defn equalVector [a b] (and
                          (vect? a)
                          (vect? b)
                          (== (count a) (count b))))
(defn baseOpV [f] (fn [a b] {:pre  [(equalVector a b)]
                             :post [(vect? %)]}
                    ((fn partOperation [a b pos] (if (>= pos (count a))
                                                   a
                                                   (assoc (partOperation a b (+ pos 1)) pos (f (nth a pos) (nth b pos)))))
                     a b 0)))

(defn getTensor [a value] (if (not= (type a) clojure.lang.PersistentVector)
                            value
                            (vec (for [x (range (count a))] (getTensor (nth a x) value)))))

(defn leftHoldVector [f defaultValue] (fn [& arr] (if (> (count arr) 1)
                                                    (apply (leftHold (baseOpV f)) arr)
                                                    ((baseOpV f) (getTensor (nth arr 0) defaultValue) (nth arr 0)))))

(def v+ (leftHoldVector + 0))
(def v- (leftHoldVector - 0))
(def v* (leftHoldVector * 1))
(def vd (leftHoldVector / 1))

(defn scalar [& arr] (reduce + (apply v* arr)))

(defn baseVect [a b] {:pre  [(and
                               (equalVector a b)
                               (== (count a) (count b) 3))]
                      :post [(and
                               (vect? %)
                               (== (count %) 3))]}
  [(- (* (nth a 1) (nth b 2)) (* (nth a 2) (nth b 1)))
   (- (* (nth a 2) (nth b 0)) (* (nth a 0) (nth b 2)))
   (- (* (nth a 0) (nth b 1)), (* (nth a 1) (nth b 0)))])

(def vect (leftHold baseVect))

(defn baseV*S [a scal] {:pre  [(and
                                 (vect? a)
                                 (number? scal))]
                        :post [(equalVector a %)]}
  (v* a (vec (repeat (count a) scal))))

(def v*s (leftHold baseV*S))

(defn matrix? [a] (and
                    (= (type a) clojure.lang.PersistentVector)
                    (every? #(= (type %) clojure.lang.PersistentVector) a)
                    (or (empty? a) (apply = (map #(count %) a)))
                    (every? (fn [v] (every? #(number? %) v)) a)))
(defn equalMatrix [a b] (and
                          (matrix? a)
                          (matrix? b)
                          (== (count a) (count b))
                          (= (count (nth a 0)) (count (nth b 0)))))

(defn baseOpM [f] (fn [a b] ((fn partOperation [a b pos] {:pre  [(equalMatrix a b)]
                                                          :post [(equalMatrix % a)]}
                               (if (>= pos (count a)) a
                                                       (assoc (partOperation a b (+ pos 1)) pos (
                                                                                                  (baseOpV f) (nth a pos)
                                                                                                  (nth b pos))))
                               ) a b 0)))

(defn leftHoldMatrix [f defaultValue] (fn [& arr] (if (> (count arr) 1)
                                                    (apply (leftHold (baseOpM f)) arr)
                                                    ((baseOpM f) (getTensor (nth arr 0) defaultValue) (nth arr 0)))))

(def m+ (leftHoldMatrix + 0))

(def m- (leftHoldMatrix - 0))

(def m* (leftHoldMatrix * 1))

(def md (leftHoldMatrix / 1))

(defn baseM*S [a scal] {:pre  [(and
                                 (matrix? a)
                                 (number? scal)
                                 )
                               ]
                        :post [(matrix? %)
                               ]} (m* a
                                      (vec (repeat (count a)
                                                   (vec (repeat (count (nth a 0)) scal))))))

(def m*s (leftHold baseM*S))

(defn transpose [m] {:pre  [(matrix? m)]
                     :post [(matrix? %)]} (
                        (fn partCalculation [i j] (if (>= i (count m))
                                                    (vec (repeat (count (nth m 0))
                                                                 (vec (repeat (count m) 0))))
                                                    (if (>= j (count (nth m 0)))
                                                      (partCalculation (+ i 1) 0)
                                                      (let [curres (partCalculation i (+ j 1))]
                                                        (assoc curres j (
                                                                          assoc (nth curres j) i
                                                                                               (nth (nth m i) j))))))
                          ) 0 0))

(defn m*v [m v] {:pref [(and
                          (matrix? m)
                          (vect? v)
                          (= (count (nth m 0)) (count v)))]
                 :post [(and
                          (vect? v)
                          (== (count m) (count %)))]} (
                            (fn partCalculation [a v pos] (if (>= pos (count a))
                                                            (vec (repeat (count a) 0))
                                                            (let [res (partCalculation a v (+ pos 1))]
                                                              (assoc res pos (apply +
                                                                                    (v* (nth a pos) v)
                                                                                    ))))) m v 0))

(defn baseM*M [m1 m2] {:pre  [(and
                                (matrix? m1)
                                (matrix? m2)
                                (= (count (nth m1 0)) (count m2))
                                )
                              ]
                       :post [(and
                                (matrix? %)
                                (== (count %) (count m1))
                                (== (count (nth % 0)) (count (nth m2 0))))]} (
                                  (fn partCalculation [m1 m2 pos] (if (>= pos (count m2))
                                                                    (vec (repeat (count m2)
                                                                                 (vec (repeat (count (nth m1 0)) 0))
                                                                                 ))
                                                                    (let [res (partCalculation m1 m2 (+ pos 1))]
                                                                      (assoc res pos (m*v (transpose m1) (nth m2 pos))))))
                                                        m2 m1 0))

(def m*m (leftHold baseM*M))

(defn getDepth [a] (cond
                     (number? a) 0
                     (every? #(number? %) a) 1
                     :else (+ (getDepth (nth a 0)) 1)))

(defn equalArray [a b] (cond
                         (and (number? a) (number? b)) true
                         (not= (type a) (type b)) false
                         (= (type a) clojure.lang.PersistentVector) (and
                                                                      (= (count a) (count b))
                                                                      (every? true? (for [x (range (count a))] (equalArray (nth a x) (nth b x)))))
                         :else false))
(defn isTensor [a] (cond
                     (number? a) true
                     (= (type a) clojure.lang.PersistentVector) (and
                                                                  (every? true? (for [x (range (count a))] (isTensor (nth a x))))
                                                                  (every? true? (for [x (range 1 (count a))] (equalArray (nth a x) (nth a (dec x))))))
                     :else false))


(defn broadcast [a b] (if (>= (getDepth a) (getDepth b))
                      a
                      (let [part (broadcast a (nth b 0))]
                        (vec (repeat (count b) part)))))

(defn baseOpTen [f] (fn [a b]
                       ((fn partOperation [a b] {:pre[(and
                                                        (isTensor a)
                                                        (isTensor b)
                                                        (equalArray a b))]
                                                 } (if (number? a)
                                                        (f a b)
                                                        ((fn curloop [pos] (if (< pos 0)
                                                                             (vector)
                                                                             (let [res (curloop (- pos 1))]
                                                                               (conj res (partOperation (nth a pos) (nth b pos)))))
                                                           ) (- (count a) 1))))
                        (broadcast a b) (broadcast b a))))

(defn leftHoldTensor [f defaultValue] (fn [& arr] (if (> (count arr) 1)
                                                    (apply (leftHold (baseOpTen f)) arr)
                                                    ((baseOpTen f) (getTensor (nth arr 0) defaultValue) (nth arr 0)))))
(def b+ (leftHoldTensor + 0))

(def b- (leftHoldTensor - 0))

(def b* (leftHoldTensor * 1))

(def bd (leftHoldTensor / 1))


