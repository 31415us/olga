#!/bin/sh

for name in "The Card Players" "No. 5, 1948" "Woman III" "Le Rêve" "Portrait of Adele Bloch-Bauer I"
do
    echo "doing ${name}"
    ant -Dname="${name}" run
    echo "done ${name}"
done

echo "done all"

return 0

