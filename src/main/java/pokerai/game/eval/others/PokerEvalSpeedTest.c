//You'll need to include poker eval and link against it.
//time() needs to return the time in seconds.
//By Timmy, http://pokerai.org/pf3

void testPokerEval()
{
    size_t evaluations = 0;
    CardMask hand;
    float startTime = time();
    unsigned long int sum = 0;



    for (int h1 = 0; h1 < 13; h1++)
        for (int h2 = 0; h2 < 13; h2++)
            for (int h3 = 0; h3 < 13; h3++)
                for (int h4 = 0; h4 < 13; h4++)
                    for (int h5 = 0; h5 < 13; h5++)  if (h5 != h2)
                            for (int h6 = 0; h6 < 13; h6++) if (h6 != h3)
                                    for (int h7 = 0; h7 < 13; h7++) if (h7 != h4)
                                        {
                                            CardMask_RESET(hand);

                                            CardMask_SET(hand, h1);
                                            CardMask_SET(hand, 13+h2);
                                            CardMask_SET(hand, 26+h3);
                                            CardMask_SET(hand, 39+h4);
                                            CardMask_SET(hand, 13+h5);
                                            CardMask_SET(hand, 26+h6);
                                            CardMask_SET(hand, 39+h7);

                                            //std::cout << h1 << " " << h2 << " " << h3 << " " << h4 << " " << h5 << " " << h6 << " " << h7 << "\n";

                                            //Deck_printMask(hand);
                                            //std::cout << "\n";

                                            ++evaluations;
                                            sum += Hand_EVAL_N(hand, 7);
                                        }

    std::cout << sum << "\n";
    float totalTime = (time() - startTime);
    float handsPerSec = evaluations / totalTime;
    std::cout << "Poker Eval Test\n";
    std::cout << evaluations << " hands in " << totalTime << " seconds.\n";
    std::cout << " --- Hands per second: " << std::fixed << handsPerSec << "\n\n";
}