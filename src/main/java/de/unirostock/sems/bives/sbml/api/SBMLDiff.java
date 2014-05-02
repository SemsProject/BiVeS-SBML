/**
 * 
 */
package de.unirostock.sems.bives.sbml.api;

import java.io.File;
import java.io.IOException;

import org.jdom2.JDOMException;

import de.unirostock.sems.bives.api.Diff;
import de.unirostock.sems.bives.ds.graph.GraphTranslator;
import de.unirostock.sems.bives.ds.graph.GraphTranslatorDot;
import de.unirostock.sems.bives.ds.graph.GraphTranslatorGraphML;
import de.unirostock.sems.bives.ds.graph.GraphTranslatorJson;
import de.unirostock.sems.bives.exception.BivesConnectionException;
import de.unirostock.sems.bives.exception.BivesDocumentConsistencyException;
import de.unirostock.sems.bives.markup.Typesetting;
import de.unirostock.sems.bives.markup.TypesettingHTML;
import de.unirostock.sems.bives.markup.TypesettingMarkDown;
import de.unirostock.sems.bives.markup.TypesettingReStructuredText;
import de.unirostock.sems.bives.sbml.algorithm.SBMLConnector;
import de.unirostock.sems.bives.sbml.algorithm.SBMLDiffInterpreter;
import de.unirostock.sems.bives.sbml.algorithm.SBMLGraphProducer;
import de.unirostock.sems.bives.sbml.exception.BivesSBMLParseException;
import de.unirostock.sems.bives.sbml.parser.SBMLDocument;
import de.unirostock.sems.xmlutils.ds.TreeDocument;
import de.unirostock.sems.xmlutils.exception.XmlDocumentParseException;

/**
 * The Class SBMLDiff to compare two SBML models.
 *
 * @author Martin Scharm
 */
public class SBMLDiff extends Diff
{
	
	/** The original document. */
	protected SBMLDocument doc1;
	
	/** The modified document. */
	protected SBMLDocument doc2;



	/** The graph producer. */
	protected SBMLGraphProducer graphProducer;
	
	/** The interpreter. */
	protected SBMLDiffInterpreter interpreter;
	
	/**
	 * Instantiates a new SBML differ.
	 *
	 * @param a the file containing the original SBML model
	 * @param b the file containing the modified SBML model
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 * @throws BivesDocumentConsistencyException the bives document consistency exception
	 * @throws XmlDocumentParseException the xml document parse exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws JDOMException the jDOM exception
	 */
	public SBMLDiff (File a, File b) throws BivesSBMLParseException, BivesDocumentConsistencyException, XmlDocumentParseException, IOException, JDOMException
	{
		super (a, b);
		doc1 = new SBMLDocument (treeA);
		doc2 = new SBMLDocument (treeB);
	}
	
	/**
	 * Instantiates a new SBML differ.
	 *
	 * @param a the XML code representing the original SBML model
	 * @param b the XML code representing the modified SBML model
	 * @throws XmlDocumentParseException the xml document parse exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws JDOMException the jDOM exception
	 * @throws BivesSBMLParseException the bives sbml parse exception
	 * @throws BivesDocumentConsistencyException the bives document consistency exception
	 */
	public SBMLDiff (String a, String b) throws XmlDocumentParseException, IOException, JDOMException, BivesSBMLParseException, BivesDocumentConsistencyException
	{
		super (a, b);
		doc1 = new SBMLDocument (treeA);
		doc2 = new SBMLDocument (treeB);
	}

	/**
	 * Instantiates a new SBML differ.
	 *
	 * @param a the tree document representing the original model
	 * @param b the tree document representing the modified model
	 * @throws BivesDocumentConsistencyException 
	 * @throws BivesSBMLParseException 
	 */
	public SBMLDiff (TreeDocument a, TreeDocument b) throws BivesSBMLParseException, BivesDocumentConsistencyException
	{
		super(a, b);
		doc1 = new SBMLDocument (treeA);
		doc2 = new SBMLDocument (treeB);
	}
	
	/**
	 * Instantiates a new SBML differ.
	 */
	public SBMLDiff (SBMLDocument a, SBMLDocument b)
	{
		super (a.getTreeDocument (), b.getTreeDocument ());
		doc1 = a;
		doc2 = b;
	}




	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Diff#mapTrees()
	 */
	@Override
	public boolean mapTrees() throws BivesConnectionException
	{
		SBMLConnector con = new SBMLConnector (doc1, doc2);
		con.findConnections ();
		connections = con.getConnections();
		
		treeA.getRoot ().resetModifications ();
		treeA.getRoot ().evaluate (connections);
		
		treeB.getRoot ().resetModifications ();
		treeB.getRoot ().evaluate (connections);
		
		return true;
	}


	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Diff#getCRNGraphML()
	 */
	@Override
	public String getCRNGraphML()
	{
		if (graphProducer == null)
			graphProducer = new SBMLGraphProducer (connections, doc1, doc2);
		return new GraphTranslatorGraphML ().translate (graphProducer.getCRN ());
	}




	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Diff#getReport(de.unirostock.sems.bives.markup.Typesetting)
	 */
	@Override
	public String getReport(Typesetting ts)
	{
		if (interpreter == null)
		{
			interpreter = new SBMLDiffInterpreter (connections, doc1, doc2);
			interpreter.interprete ();
		}
		return ts.typeset (interpreter.getReport ());
	}




	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Diff#getMarkDownReport()
	 */
	@Override
	public String getMarkDownReport()
	{
		if (interpreter == null)
		{
			interpreter = new SBMLDiffInterpreter (connections, doc1, doc2);
			interpreter.interprete ();
		}
		return new TypesettingMarkDown ().typeset (interpreter.getReport ());
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Diff#getReStructuredTextReport()
	 */
	@Override
	public String getReStructuredTextReport ()
	{
		if (interpreter == null)
		{
			interpreter = new SBMLDiffInterpreter (connections, doc1, doc2);
			interpreter.interprete ();
		}
		return new TypesettingReStructuredText ().typeset (interpreter.getReport ());
	}




	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Diff#getHTMLReport()
	 */
	@Override
	public String getHTMLReport()
	{
		if (interpreter == null)
		{
			interpreter = new SBMLDiffInterpreter (connections, doc1, doc2);
			interpreter.interprete ();
		}
		return new TypesettingHTML ().typeset (interpreter.getReport ());
	}



	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Diff#getCRNGraph(de.unirostock.sems.bives.ds.graph.GraphTranslator)
	 */
	@Override
	public Object getCRNGraph (GraphTranslator gt) throws Exception
	{
		if (graphProducer == null)
			graphProducer = new SBMLGraphProducer (connections, doc1, doc2);
		return gt.translate (graphProducer.getCRN ());
	}


	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Diff#getCRNDotGraph()
	 */
	@Override
	public String getCRNDotGraph ()
	{
		if (graphProducer == null)
			graphProducer = new SBMLGraphProducer (connections, doc1, doc2);
		return new GraphTranslatorDot ().translate (graphProducer.getCRN ());
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Diff#getCRNJsonGraph()
	 */
	@Override
	public String getCRNJsonGraph ()
	{
		if (graphProducer == null)
			graphProducer = new SBMLGraphProducer (connections, doc1, doc2);
		return new GraphTranslatorJson ().translate (graphProducer.getCRN ());
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Diff#getHierarchyGraph(de.unirostock.sems.bives.ds.graph.GraphTranslator)
	 */
	@Override
	public Object getHierarchyGraph (GraphTranslator gt)
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Diff#getHierarchyGraphML()
	 */
	@Override
	public String getHierarchyGraphML ()
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Diff#getHierarchyDotGraph()
	 */
	@Override
	public String getHierarchyDotGraph ()
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see de.unirostock.sems.bives.api.Diff#getHierarchyJsonGraph()
	 */
	@Override
	public String getHierarchyJsonGraph ()
	{
		return null;
	}

}
