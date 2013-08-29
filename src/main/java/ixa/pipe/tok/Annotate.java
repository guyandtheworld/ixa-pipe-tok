/*
 * Copyright 2013 Rodrigo Agerri

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package ixa.pipe.tok;

import ixa.kaflib.KAFDocument;
import ixa.kaflib.WF;
import ixa.pipe.seg.SentenceSegmenter;

import java.io.IOException;

public class Annotate {

  /**
   * This method uses the Apache OpenNLP Sentence Detector and Tokenizer to
   * produce tokenized text by sentences.
   * 
   * For every line of text the method receives, it creates an array of
   * segmented sentences, and an array of Tokens.
   * 
   * It fills the kaf object with the word forms element <wf> corresponding to
   * each of the tokens.
   * 
   * @param line
   *          of string
   * @param KAF
   *          object. This object is used to take the output data and convert it
   *          to KAF, returning an XML document in a string.
   */

  int noSents = 0;
  int offsetCounter = 0;

  public void annotateTokensToKAF(String line, SentenceSegmenter sentDetector,
      TokTokenizer toker, KAFDocument kaf) throws IOException {

    String sentences[] = sentDetector.segmentSentence(line);

    // get linguistic annotations
    for (String sent : sentences) {

      String tokens[] = toker.tokenize(sent);

      // get sentence counter
      noSents = noSents + 1;
      // offsets counters
      int current_index = 0;
      int previous_index = 0;
      for (int i = 0; i < tokens.length; i++) {
        // get offsets
        current_index = line.indexOf(tokens[i], previous_index);
        int offset = offsetCounter + current_index;
        WF wf = kaf.newWF(tokens[i], offset);
        wf.setSent(noSents);
        previous_index = current_index + tokens[i].length();
      }
    }
    offsetCounter += line.length();
  }

}
