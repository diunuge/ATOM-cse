package com.project.traceability.db;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.graphdb.index.ReadableIndex;

import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;

/**
 * Model to add data to graph DB and visualize it.
 * 
 * @author Thanu
 * 
 */
public class GraphDB extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Define relationship type.
	 * 
	 * @author Thanu
	 * 
	 */
	private static enum RelTypes implements RelationshipType {
		SUB_ELEMENT("Sub Element"), SOURCE_TO_TARGET("Source To Target");
		private final String value;

		private RelTypes(String val) {
			this.value = val;
		}

		@Override
		public String toString() {
			return value;
		}

		public String getValue() {
			return value;
		}

		public static RelTypes parseEnum(final String val) {

			RelTypes relType = null;
			for (RelTypes type : RelTypes.values()) {
				if (type.getValue().equals(val)) {
					relType = type;
				}
			}
			return relType;
		}
	}

	/**
	 * Define Node types.
	 * 
	 * @author Thanu
	 * 
	 */
	private static enum NodeTypes implements RelationshipType {
		CLASS("Class"), FIELD("Field"), METHOD("Method"), UMLATTRIBUTE(
				"UMLAttribute"), UMLOPERATION("UMLOperation");
		private final String value;

		private NodeTypes(String val) {
			this.value = val;
		}

		@Override
		public String toString() {
			return value;
		}

		public String getValue() {
			return value;
		}

		public static NodeTypes parseEnum(final String val) {

			NodeTypes nodeType = null;
			for (NodeTypes type : NodeTypes.values()) {
				if (type.getValue().equals(val)) {
					nodeType = type;
				}
			}
			return nodeType;
		}
	}

	GraphDatabaseService graphDb;
	Relationship relationship;
	private JPanel contentPane;

	public void initiateGraphDB() {

		graphDb = new GraphDatabaseFactory()
				.
				// .newEmbeddedDatabase("C:\\Users\\Thanu\\Documents\\Neo4j\\atomdb.graphdb")
				newEmbeddedDatabaseBuilder(
						"D:\\Neo4j Community\\atomdb.graphdb")
				.setConfig(GraphDatabaseSettings.node_keys_indexable, "NODE_ID")
				.setConfig(GraphDatabaseSettings.node_auto_indexing, "true")
				.newGraphDatabase();
		Transaction tx = graphDb.beginTx();
		try {
			cleanUp(graphDb);
			tx.success();

		} finally {
			tx.finish();
		}
		registerShutdownHook(graphDb);
	}

	public void addNodeToGraphDB(Map<String, ArtefactElement> aretefactElements) {
		Transaction tx = graphDb.beginTx();
		try {
			
			Iterator<Entry<String, ArtefactElement>> iterator = aretefactElements
					.entrySet().iterator();
			// graphDb.schema()
			// .constraintFor(
			// DynamicLabel.label(iterator.next().getValue()
			// .getType()))
			// .assertPropertyIsUnique("NODE_ID").create();

			while (iterator.hasNext()) {

				Map.Entry pairs = iterator.next();
				ArtefactElement artefactElement = (ArtefactElement) pairs
						.getValue();
				Label myLabel = DynamicLabel.label(artefactElement.getType());

				Node n = graphDb.createNode();
				// IndexManager index = graphDb.index();
				// Index<Node> artefacts = index.forNodes("Artefacts ID");
				n.addLabel(myLabel);
				n.setProperty("NODE_ID", artefactElement.getArtefactElementId());
				n.setProperty("Name", artefactElement.getName());
				n.setProperty("Type", artefactElement.getType());
				// artefacts.add(n, "ID", n.getProperty("ID"));
				List<ArtefactSubElement> artefactsSubElements = artefactElement
						.getArtefactSubElements();
				for (int i = 0; i < artefactsSubElements.size(); i++) {
					Node m = graphDb.createNode();
					myLabel = DynamicLabel.label(artefactsSubElements.get(i)
							.getType());
					m.addLabel(myLabel);
					m.setProperty("NODE_ID", artefactsSubElements.get(i)
							.getSubElementId());
					m.setProperty("Name", artefactsSubElements.get(i).getName());
					m.setProperty("Type", artefactsSubElements.get(i).getType());
					if (null != artefactsSubElements.get(i).getVisibility()) {
						m.setProperty("Visibility", artefactsSubElements.get(i)
								.getVisibility());
					}
					if (null != artefactsSubElements.get(i).getReturnType()) {
						m.setProperty("Return Type", artefactsSubElements
								.get(i).getReturnType());
					}
					relationship = n.createRelationshipTo(m,
							RelTypes.SUB_ELEMENT);
					relationship.setProperty("message",
							RelTypes.SUB_ELEMENT.getValue());
				}

			}
			tx.success();

		} finally {
			tx.finish();
		}
	}

	public void addRelationTOGraphDB(List<String> relation) {
		Transaction tx = graphDb.beginTx();
		try {
			// IndexManager index = graphDb.index();
			// Index<Node> artefacts = index.forNodes("Artefacts ID");

			// Get the Node auto index
			ReadableIndex<Node> autoNodeIndex = graphDb.index()
					.getNodeAutoIndexer().getAutoIndex();
			for (int i = 0; i < relation.size(); i++) {
				// IndexHits<Node> hits = artefacts.get("ID", relation.get(i));
				// Node source = hits.getSingle();//
				// IteratorUtil.firstOrNull(hits);//
				// hits.getSingle();

				/*System.out.println("Nodes: "
						+ autoNodeIndex.get("NODE_ID", relation.get(i)).size());*/
				Node source = autoNodeIndex.get("NODE_ID", relation.get(i))
						.getSingle();

				// hits = artefacts.get("ID", relation.get(++i));
				// Node target = hits.getSingle();
				/*System.out
						.println("Nodes: "
								+ relation.get(++i).toString()
								+ " "
								+ +autoNodeIndex
										.get("NODE_ID", relation.get(i)).size());*/

				Node target = autoNodeIndex.get("NODE_ID", relation.get(i))
						.getSingle();

				relationship = source.createRelationshipTo(target,
						RelTypes.SOURCE_TO_TARGET);
				relationship.setProperty("message",
						RelTypes.SOURCE_TO_TARGET.getValue());
			}
			tx.success();
		} finally {
			tx.finish();
		}
	}

	public void drawGraph() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GraphDB frame = new GraphDB();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
		// Registers a shutdown hook for the Neo4j instance so that it
		// shuts down nicely when the VM exits (even if you "Ctrl-C" the
		// running application).
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		});
		// Runtime.getRuntime().addShutdownHook(new Thread() {
		// @Override
		// public void run() {
		// graphDb.shutdown();
		// // try {
		// // FileUtils.deleteRecursively(new
		// // File("C:\\Users\\Thanu\\Documents\\Neo4j\\atomdb.graphdb"));
		// // } catch (IOException e) {
		// // e.printStackTrace();
		// // }
		// }
		// });
	}

	@SuppressWarnings("deprecation")
	public void cleanUp(final GraphDatabaseService graphDb) {
		// final Index<Node> nodeIndex) {
		for (Node node : graphDb.getAllNodes()) {
			for (Relationship rel : node.getRelationships()) {
				rel.delete();
			}
			// nodeIndex.remove(node);
			node.delete();
		}
	}

	public GraphDB() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 794, 653);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		final JTextArea queryTextArea = new JTextArea();
		queryTextArea.setBounds(25, 40, 702, 109);
		queryTextArea.setBorder(new EmptyBorder(5, 5, 5, 5));
		queryTextArea.setText("start n=node(*) return n, n.name");
		contentPane.add(queryTextArea);

		JLabel cypherQueryLabel = new JLabel("Cypher Query");
		cypherQueryLabel.setBounds(25, 12, 105, 23);
		contentPane.add(cypherQueryLabel);

		final JTextArea resultTextArea = new JTextArea();
		resultTextArea.setBounds(25, 181, 702, 341);
		resultTextArea.setEditable(false);
		resultTextArea.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.add(resultTextArea);

		JLabel resultLabel = new JLabel("Result:");
		resultLabel.setBounds(25, 323, 55, 23);

		contentPane.add(resultLabel);

		JButton btnNewButton_2 = new JButton("Execute Query");
		btnNewButton_2.setBounds(274, 540, 176, 41);
		btnNewButton_2.setVisible(true);
		contentPane.add(btnNewButton_2);

		btnNewButton_2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				//graphDb = new EmbeddedGraphDatabase("D:\\Neo4j Community\\atomdb.graphdb");
				registerShutdownHook(graphDb);
				ExecutionEngine engine = new ExecutionEngine(graphDb);
				ExecutionResult result = engine.execute(queryTextArea.getText());
				String rows = null;
				for (Map<String, Object> row : result) {
					for (Entry<String, Object> column : row.entrySet()) {
						rows += column.getKey() + ": " + column.getValue()
								+ "; ";
					}
					rows += "\n";
				}
				resultTextArea.setText(rows);
			}
		});
	}

}
