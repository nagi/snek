(ns snek.core-test
  (:require [clojure.test :refer :all]
            [snek.core :refer :all]))

(deftest snake-movment
  (testing "move a snake north"
    (is
     (= (:position (snake-move (snake-new [5 5] :north))) [4 5])))
  (testing "move a snake south"
    (is
     (= (:position (snake-move (snake-new [5 5] :south))) [6 5])))
  (testing "move a static west"
    (is
     (= (:position (snake-move (snake-new [5 5] :west))) [5 4])))
  (testing "move a snake east"
    (is
     (= (:position (snake-move (snake-new [5 5] :east))) [5 6])))
  (testing "move a stopped snake"
    (is
     (= (:position (snake-move (snake-new [5 5] :stopped))) [5 5]))))
