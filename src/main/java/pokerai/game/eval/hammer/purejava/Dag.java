package pokerai.game.eval.hammer.purejava;

import pokerai.game.eval.hammer.HandEvaluator;
import pokerai.game.eval.hammer.classes.EquivalenceClasses;
import pokerai.game.eval.hammer.classes.EquivalenceClass;
import pokerai.game.eval.hammer.generator.HandEvalGenerator;
import pokerai.game.eval.hammer.generator.CardEvaluator;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class Dag {
	private static final String DAGFILE = "dag.ser";
	private static int SIZE = 76154;
	public static final DAGNode[] nodes;

	static {
		File f = new File(DAGFILE);
		if (!f.exists()) {
			System.out.println("Dag do not exist, this is first time run. Generating...");
			CardEvaluator eval = HandEvalGenerator.buildCardSetDAG(7, 13);
			try {
				eval.externalize(f);
			} catch (IOException ioe) {
				throw new Error(ioe);
			}
		}
		nodes = new DAGNode[SIZE];
		int[][] prenext = new int[SIZE][13];
		int[] preflushclass = new int[SIZE];
		int[] prerankclass = new int[SIZE];
		int line = 0;
		try {
			FileInputStream fis = new FileInputStream(DAGFILE);
			ObjectInputStream ois = new ObjectInputStream(fis);
			for (int i = SIZE-1; i >= 0; i--) {
				int id = ois.readInt();
				for (int j=0; j < 13; j++) {
					prenext[id][j] = ois.readInt();
				}
				preflushclass[id] = ois.readInt();
				prerankclass[id] = ois.readInt();
				line++;
			}
			ois.close();
		} catch (IOException ioe) {
			throw new Error(ioe);
		}
		EquivalenceClasses eqClasses = EquivalenceClasses.getInstance();
		EquivalenceClass flushclass, rankclass;
		for (int i = line -1; i >= 0; i--) {
			DAGNode[] next = new DAGNode[13];
			if (preflushclass[i] > 0) {
				flushclass = eqClasses.getEquivalenceClass(preflushclass[i]);
			} else {
				flushclass = null;
			}
			if (prerankclass[i] > 0) {
				rankclass = eqClasses.getEquivalenceClass(prerankclass[i]);
			} else {
				rankclass = null;
			}
			for (int j=0; j < 13; j++) {
				if (prenext[i][j] > 0) {
					next[j] = nodes[prenext[i][j]];
				} else {
					next[j] = null;
				}
			}
			nodes[i] = new DAGNode(i, flushclass, rankclass, next);
		}
		System.out.println("Loaded " + line + " nodes ");
	}
	
	public static void print() {
		for (DAGNode n : nodes) {
			System.out.println(n.getId() + " " + n.flushclass + " " + n.rankclass);
			for (int j=0; j < 13; j++) {
				System.out.print(" " + n.next[j].getId());
 			}
			System.out.println();
		}
	}
}
