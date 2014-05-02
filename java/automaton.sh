#!/bin/sh

for name in "Portrait of Dr. Gachet" "Three Studies of Lucian Freud" "Bal du moulin de la Galette" "Garçon à la pipe" "The Scream" "Flag" "Nude, Green Leaves and Bust"
do
	ant -Dname "${name}"
	echo "Done for ${name}\n"
done
echo "Done !\n"
return 0