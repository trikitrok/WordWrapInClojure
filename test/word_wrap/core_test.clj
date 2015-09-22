(ns word-wrap.core-test
  (:use midje.sweet)
  (:use [word-wrap.core]))

(facts
  "about wrapping words"

  (fact
    "a text that fits in the given columns number are not wrapped"
    (wrap "koko koko" 9) => "koko koko"
    (wrap "ko" 9) => "ko"
    (wrap "" 9) => "")

  (fact
    "a text without spaces that doesn't fit in the given columns number are wrapped"
    (wrap "kokokoko" 4) => "koko\nkoko")

  (fact
    "a text with spaces that doesn't fit in the given columns number are wrapped
    at the space that is closest to the maximum column"
    (wrap "koko koko" 7) => "koko\nkoko"
    (wrap "koko koko koko" 12) => "koko koko\nkoko"
    (wrap "koko koko koko koko koko koko" 12) => "koko koko\nkoko koko\nkoko koko"
    (wrap
      "This koko should be easy unless there are hidden, or not so hidden, obstacles. Let's start!"
      12) => "This koko\nshould be\neasy unless\nthere are\nhidden, or\nnot so\nhidden,\nobstacles.\nLet's start!"))
