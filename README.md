locness
=======

> "Measuring software productivity by lines of code is like measuring progress on an airplane by how much it weighs." – Bill Gates.

This project is a simulation as part of a code experiment on the size of code (LOC). This is not meant to be actually used by anyone, and this code does not reflect the way I usually code.

> "The number of lines of program code is wonderful metric. It's so easy to measure and almost impossible to interpret."
- http://c2.com/cgi/wiki?LinesOfCode

# A Matter Of Size

## Abstract
Size, from the number of lines of code to the number of people in a project, is one of the most obvious metric, but what does it really tell us? 

- Is the size of code a measure of functional scope and complexity, or could it be a measure of waste? 
- Does the size of the team measure the capacity to deliver more value, or a measure of waste and vanity? 

We'll go through an experiment with code to show how the same feature can be implemented with very little code or with a lot of code. We'll also analyze the effect of the size of the teams.

## My project is bigger than yours! (No, indeed your project is buggier than mine)

Some questions I had in mind recently:
- Why do I have the impression that our software was supporting approximately equivalent feature scope when we were 6, 15 or 90 developers? With 200KLOC, 1MLOC or 2MLOC? All that in a very similar domain...
- Why do I always have the feeling that the bigger the project and the code size, the more they can brag about it?
- Why do I always have the feeling that the more we have to do in a small team, the smarter we get? 

And a few remarks I know for sure:
- I certainly have the feeling that the bigger, the more problems!
- I also have already verified in practice that we could rewrite a few thousands LOC into just a few hundreds LOC.

# Experiment 

## The Protocol
The experiment consists in doing what we usually never do: write the same application *twice*, change request after change request, as if done by two different teams A and B in the same company.

To be realistic enough, both projects follow similar constraints:
- same language (Java, version 7 available)
- same code style and code formatting (same company)
- same specs, clear enough
- realistic code you could really see in any company (not too dumb for the sake of it); I only introduced (deliberatelythat is) smells and unelegant code that I have already met for real in my past experience. 

The two different teams A and B are defined as follow: 
- A: Rather average team with more juniors. *Attempts* (as much as possible) to do Clean Code (no abbreviation, naming matters, extract visible duplication…) and to follow the 4 principles of Simple Design
- B: More senior team with more passionate developers; Also follows Clean Code and the 4 principles of Simple Design, and is also quite comfortable in design, polymorphism,  patterns, DDD and letting the business domain drive the code and the refactoring.

## Measurements

I simply use the [Eclipse plugin metrics](http://metrics.sourceforge.net/).

Out of all the metrics it provides, I focus on just a few:
- Total LOC
- Method LOC
- McCabe Cyclomatic Complexity

In addition, following an idea found in the [c2 wiki](http://c2.com/cgi/wiki?LinesOfCode), a fourth metrics seems interesting:
- size of the code once zipped

## Known limits

This experiment is done by a single person (me) hence is limited to my (limited) ability to simulate the way of working/thinking of other developers, as far as I can infer from all the legacy code I've seen in the past.

# Results

The results will be presented at [Agile France 2014](http://2014.conference-agile.fr).




