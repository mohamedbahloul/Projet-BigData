import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class ThirdJob {
    public static class TopKMapper
            extends Mapper<Text, NullWritable, IntWritable, Text> {

        @Override
        public void map(Text key, NullWritable value, Context context) throws IOException, InterruptedException {
            if (key.toString().contains("m_")) {
                String[] split = key.toString().split("_");
                String cards = split[0];
                String month = split[2];
                String wins = split[5];
                context.write(new IntWritable(Integer.parseInt(month)), new Text(cards + "_" + wins));
            }
        }
    }

    public static class TopKReduecr extends Reducer<IntWritable, Text, Text, NullWritable> {
        private static final int K = 5;
        private static final List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December");
        private ArrayList<TreeMap<Integer, String>> topKs = new ArrayList<TreeMap<Integer, String>>();

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            for (int i = 0; i < 12; i++) {
                topKs.add(new TreeMap<Integer, String>());
            }
        }

        @Override
        public void reduce(IntWritable key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {

            Integer month = Integer.parseInt(key.toString());
            TreeMap<Integer, String> topK = topKs.get(month);

            for (Text value : values) {
                String[] split = value.toString().split("_");
                String deck = split[0];
                Integer wins = Integer.parseInt(split[1]);
                if (topK.containsKey(wins)) {
                    String old_decks = topK.get(wins);
                    topK.remove(wins);
                    topK.put(wins, old_decks + " , " + deck);
                } else {
                    if (topK.size() < K)
                        topK.put(wins, deck);
                    else {
                        Integer first = topK.firstKey();
                        if (wins.intValue() > first.intValue()) {
                            topK.remove(first);
                            topK.put(wins, deck);
                        }
                    }
                }
            }

            context.write(new Text("Month: " + months.get(month)), NullWritable.get());
            for (Integer wins : topK.descendingKeySet()) {
                context.write(new Text("\tWins: " + wins + " Decks: " + topK.get(wins)), NullWritable.get());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job3 = Job.getInstance(conf, "ThirdJob");
        job3.setNumReduceTasks(1);
        job3.setJarByClass(ThirdJob.class);
        job3.setMapperClass(TopKMapper.class);
        job3.setMapOutputKeyClass(IntWritable.class);
        job3.setMapOutputValueClass(Text.class);
        job3.setReducerClass(TopKReduecr.class);
        job3.setOutputKeyClass(Text.class);
        job3.setOutputValueClass(NullWritable.class);
        job3.setOutputFormatClass(TextOutputFormat.class);
        job3.setInputFormatClass(SequenceFileInputFormat.class);
        FileInputFormat.addInputPath(job3, new Path(args[0]));
        FileOutputFormat.setOutputPath(job3, new Path(args[1]));
        System.exit(job3.waitForCompletion(true) ? 0 : 1);
    }
}