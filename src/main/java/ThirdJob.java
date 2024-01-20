import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

public class ThirdJob {
    public static class TopKMapper extends Mapper<TopK, NullWritable, Text, TopK> {

        @Override
        public void map(TopK key, NullWritable value, Context context)
                throws IOException, InterruptedException {
            if (key.getYear() != -1) {
                if (key.getMonth() != -1) {
                    context.write(new Text(key.getYear() + "_m_" + key.getMonth()), key);
                } else if (key.getWeek() != -1) {
                    context.write(new Text(key.getYear() + "_w_" + key.getWeek()), key);
                }
            } else {
                context.write(new Text("All"), key);
            }
        }
    }

    public static class TopKReducer extends Reducer<Text, TopK, Text, TopK> {
        private static final int K = 50;
        private HashMap<String, TreeMap<TopKKey, TopK>> topKsWins = new HashMap<String, TreeMap<TopKKey, TopK>>();

        @Override
        public void reduce(Text key, Iterable<TopK> values, Context context)
                throws IOException, InterruptedException {
            String date = key.toString();
            TreeMap<TopKKey, TopK> topKWins = topKsWins.get(date);
            if (topKWins == null) {
                topKWins = new TreeMap<TopKKey, TopK>();
                topKsWins.put(date, topKWins);
            }

            for (TopK value : values) {
                addToTopKByScore(topKWins, value);
            }

            for (TopKKey wins : topKWins.descendingKeySet()) {
                context.write(new Text(date), topKWins.get(wins));
            }
        }

        // private void addToTopKByFIFO(TreeMap<Integer, TopK> topK, TopK deck) {
        //     Integer wins = deck.getWins();

        //     if (topK.size() < K) {
        //         topK.put(wins, deck);
        //     } else {
        //         Integer first = topK.firstKey();
        //         if (wins.intValue() > first.intValue()) {
        //             topK.remove(first);
        //             topK.put(wins, deck);
        //         }
        //     }
        // }

        private void addToTopKByScore(TreeMap<TopKKey, TopK> topK, TopK deck) {
            TopKKey key = new TopKKey(deck.getWins(), deck.getUses(), deck.getAvgDeckDiff());

            if (topK.size() < K) {
                topK.put(key, new TopK(deck));
            } else {
                TopKKey first = topK.firstKey();
                if (key.compareTo(first) > 0) {
                    topK.remove(first);
                    topK.put(key, new TopK(deck));
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job3 = Job.getInstance(conf, "ThirdJob");
        job3.setNumReduceTasks(1);
        job3.setJarByClass(ThirdJob.class);
        job3.setMapperClass(TopKMapper.class);
        job3.setMapOutputKeyClass(Text.class);
        job3.setMapOutputValueClass(TopK.class);
        job3.setReducerClass(TopKReducer.class);
        job3.setOutputKeyClass(Text.class);
        job3.setOutputValueClass(TopK.class);
        job3.setOutputFormatClass(SequenceFileOutputFormat.class);
        job3.setInputFormatClass(SequenceFileInputFormat.class);
        FileInputFormat.addInputPath(job3, new Path(args[0]));
        FileOutputFormat.setOutputPath(job3, new Path(args[1]));
        System.exit(job3.waitForCompletion(true) ? 0 : 1);
    }
}
