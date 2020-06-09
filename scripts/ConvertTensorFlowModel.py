import tensorflow.compat.v1 as tf
import tensorflow_hub as hub
tf.disable_v2_behavior()

def save_module(url, save_path, input_type):
    with tf.Graph().as_default():
        module = hub.KerasLayer(url)
        model_input = tf.placeholder(input_type, name="input")
        model_output = tf.identity(module(model_input), name="output")
        with tf.Session() as session:
            session.run(tf.global_variables_initializer())
            tf.saved_model.simple_save(
                session,
                save_path,
                inputs={'input': model_input},
                outputs={'output': model_output},
                legacy_init_op=tf.initializers.tables_initializer(name='init_all_tables'))

save_module("https://tfhub.dev/google/universal-sentence-encoder-large/5", "models/tmp/universal-sentence-encoder", tf.string)