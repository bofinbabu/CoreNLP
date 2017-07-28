package edu.stanford.nlp.time;

import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 * Created by chaganty on 7/12/17.
 */
public class HeidelTimeTest {

  @Test
  public void runHeidelTimeEnglish() throws Exception {
    String text = "On Monday, some cataclysmic news about a a release last Christmas was released.";

    Annotation ann = new Annotation(text);
    String date = "2017-07-07";
    ann.set(CoreAnnotations.DocDateAnnotation.class, date);

    Properties defaultProps = new Properties();
    defaultProps.load(IOUtils.getInputStreamFromURLOrClasspathOrFileSystem("edu/stanford/nlp/pipeline/StanfordCoreNLP.properties"));
    Properties props = new Properties(defaultProps);
    props.setProperty(HeidelTimeAnnotator.HEIDELTIME_PATH_PROPERTY, System.getenv("HEIDELTIME_PATH"));
    props.setProperty(HeidelTimeAnnotator.HEIDELTIME_LANGUAGE_PROPERTY, "english");
    props.setProperty("annotators", "tokenize,ssplit");

    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
    pipeline.addAnnotator(new HeidelTimeAnnotator("", props));
    pipeline.annotate(ann);

    List<CoreMap> outputs = ann.get(TimeAnnotations.TimexAnnotations.class);
    Assert.assertEquals(2, outputs.size());

    Assert.assertEquals("Monday", outputs.get(0).get(TimeAnnotations.TimexAnnotation.class).text());
    Assert.assertEquals("2017-07-03", outputs.get(0).get(TimeAnnotations.TimexAnnotation.class).value());

    Assert.assertEquals("Christmas", outputs.get(1).get(TimeAnnotations.TimexAnnotation.class).text());
    Assert.assertEquals("2016-12-25", outputs.get(1).get(TimeAnnotations.TimexAnnotation.class).value());
  }

  @Test
  public void runHeidelTimeSpanish() throws Exception {
    String text = "El lunes, algunas noticias cataclísmicas sobre un lanzamiento de la Navidad pasada fueron liberadas.";

    Annotation ann = new Annotation(text);
    String date = "2017-07-07";
    ann.set(CoreAnnotations.DocDateAnnotation.class, date);

    Properties defaultProps = new Properties();
    defaultProps.load(IOUtils.getInputStreamFromURLOrClasspathOrFileSystem("edu/stanford/nlp/pipeline/StanfordCoreNLP-spanish.properties"));
    Properties props = new Properties(defaultProps);
    props.setProperty(HeidelTimeAnnotator.HEIDELTIME_PATH_PROPERTY, System.getenv("HEIDELTIME_PATH"));
    props.setProperty(HeidelTimeAnnotator.HEIDELTIME_LANGUAGE_PROPERTY, "spanish");
    props.setProperty("annotators", "tokenize,ssplit");

    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
    pipeline.addAnnotator(new HeidelTimeAnnotator("", props));
    pipeline.annotate(ann);

    List<CoreMap> outputs = ann.get(TimeAnnotations.TimexAnnotations.class);
    Assert.assertEquals(1, outputs.size()); // Unfortunately, HeidelTime doesn't get Navidad :-(

    Assert.assertEquals("El lunes", outputs.get(0).get(TimeAnnotations.TimexAnnotation.class).text());
    Assert.assertEquals("2017-07-03", outputs.get(0).get(TimeAnnotations.TimexAnnotation.class).value());

    //Assert.assertEquals("Navidad", outputs.get(1).get(TimeAnnotations.TimexAnnotation.class).text());
    //Assert.assertEquals("2016-12-25", outputs.get(1).get(TimeAnnotations.TimexAnnotation.class).value());
  }
}
