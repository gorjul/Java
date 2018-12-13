import java.io.File;
import java.io.FileInputStream;
import java.io.ByteArrayInputStream;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


Logger log = LoggerFactory.getLogger(KerasImageClassifierInJava.class);
    
//1) set he path to your image
File f = new File(path_to_image);
//2) alternatively if you get the image as a byte array you can use following
// InputStream is = new ByteArrayInputStream(byte_array);

//Use the nativeImageLoader to convert to numerical matrix
//change the size of the image
NativeImageLoader loader = new NativeImageLoader(image_height, image_width, 3);

//read image as a numpy matrix
//1) from file
INDArray img = loader.asMatrix(f);
//2) from byte array
// INDArray img = loader.asMatrix(is);

//if your data structure doesn't look like the structure you used to train your classifier, use
// Nd4j.rollAxis(INDArray, axis, new axis)
//sometimes you need to redo this step multiple times because the rollAxis only moves an axis back not to the front

//values need to be scaled between 0 and 1
DataNormalization scalar = new ImagePreProcessingScaler(0, 1);
//then call that scalar on the image dataset
scalar.transform(img);

//pass through neural net and store it in output array
INDArray output = cloud_model.output(img);

log.info("classification result {}", output);

//round the classification to an integer number
boolean result = output.getInt(0) == 1;
