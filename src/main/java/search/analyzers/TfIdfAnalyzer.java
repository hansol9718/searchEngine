package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;
import java.net.URI;

/**
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;

    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;

    // Feel free to add extra fields and helper methods.
    private IDictionary<URI, Double> normVector;

    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
        this.normVector = new ChainedHashDictionary<>();
        for (KVPair<URI, IDictionary<String, Double>> doc : documentTfIdfVectors) {
            normVector.put(doc.getKey(), norm(doc.getValue()));
        }
    }

    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    // Note: these private methods are suggestions or hints on how to structure your
    // code. However, since they're private, you're not obligated to implement exactly
    // these methods: feel free to change or modify these methods however you want. The
    // important thing is that your 'computeRelevance' method ultimately returns the
    // correct answer in an efficient manner.

    /**
     * Return a dictionary mapping every single unique word found
     * in every single document to their IDF score.
     */
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
        IDictionary<String, Double> docCount = new ChainedHashDictionary<>();
        IDictionary<String, Double> idfResult = new ChainedHashDictionary<>();
        for (Webpage page : pages) {
            ISet<String> unique = new ChainedHashSet<>();
            IList<String> words = page.getWords();
            for (String word : words) {
                unique.add(word);
            }
            for (String key : unique) {
                if (docCount.containsKey(key)) {
                    docCount.put(key, docCount.get(key) + 1.0);
                } else {
                    docCount.put(key, 1.0);
                }
            }
        }
        for (KVPair<String, Double> pair : docCount) {
            idfResult.put(pair.getKey(), Math.log(pages.size() / pair.getValue()));
        }
        return idfResult;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * The input list represents the words contained within a single document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
        IDictionary<String, Double> tfResult = new ChainedHashDictionary<>();
        for (String word : words) {
            if (tfResult.containsKey(word)) {
                double modifiedTf = (tfResult.get(word) * words.size() + 1.0) / words.size();
                tfResult.put(word, modifiedTf);
            } else {
                tfResult.put(word, 1.0 / words.size());
            }
        }
        return tfResult;
    }

    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        // Hint: this method should use the idfScores field and
        // call the computeTfScores(...) method.
        IDictionary<URI, IDictionary<String, Double>> vectors = new ChainedHashDictionary<>();
        for (Webpage page : pages) {
            IDictionary<String, Double> tfIdf = computeTfScores(page.getWords());
            for (KVPair<String, Double> pair : idfScores) {
                String word = pair.getKey();
                if (tfIdf.containsKey(word)) {
                    tfIdf.put(word, pair.getValue() * tfIdf.get(word));
                    vectors.put(page.getUri(), tfIdf);
                }
            }
        }
        return vectors;
    }

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
        // Note: The pseudocode we gave you is not very efficient. When implementing,
        // this method, you should:
        //
        // 1. Figure out what information can be precomputed in your constructor.
        //    Add a third field containing that information.
        // 2. See if you can combine or merge one or more loops.

        IDictionary<String, Double> documentVector = documentTfIdfVectors.get(pageUri);
        IDictionary<String, Double> queryVector = new ChainedHashDictionary<>();
        IDictionary<String, Double> queryTf = this.computeTfScores(query);
        for (KVPair<String, Double> pair : queryTf) {
            double queryTfIdf = 0.0;
            if (idfScores.containsKey(pair.getKey())) {
                queryTfIdf = idfScores.get(pair.getKey()) * pair.getValue();
            }
            queryVector.put(pair.getKey(), queryTfIdf);
        }
        double numerator = 0.0;
        for (KVPair<String, Double> word : queryVector) {
            double docWordScore = 0.0;
            if (documentVector.containsKey(word.getKey())) {
                docWordScore = documentVector.get(word.getKey());
            }
            numerator += docWordScore * queryVector.get(word.getKey());
        }
        double denominator = this.normVector.get(pageUri) * norm(queryVector);
        if (denominator != 0) {
            return numerator / denominator;
        } else {
            return 0.0;
        }
    }

    private double norm(IDictionary<String, Double> vector) {
        double output = 0.0;
        for (KVPair<String, Double> pair : vector) {
            double score = pair.getValue();
            output += score * score;
        }
        return Math.sqrt(output);
    }
}
