require "./non_personal_recommender"

describe NonPersonalRecommender, "#get_top5_simple_frequency" do
  it "returns the expected example values" do
    recommender = NonPersonalRecommender.new

    example_1 = [[603,0.96],[1892,0.94],[1891,0.94],[120,0.93],[1894,0.93]]
    example_2 = [[120,0.95],[122,0.95],[603,0.94],[597,0.89],[604,0.88]]
    example_3 = [[603,0.92],[597,0.90],[607,0.87],[120,0.86],[13,0.86]]

    result_1 = recommender.get_top5_simple_frequency(11)
    check_results 11, result_1, example_1

    result_2 = recommender.get_top5_simple_frequency(121)
    check_results 121, result_2, example_2

    result_3 = recommender.get_top5_simple_frequency(8587)
    check_results 8587, result_3, example_3

  end
end

def check_results movie_id, actual, expected
  puts "movie id #{movie_id}:"
  puts actual.join(",")

  (0..actual.size-1).each do |i|
    actual[i][0].should be expected[i][0]
    actual[i][1].should be_within(0.1).of(expected[i][1])
  end

  puts "---------------------------------"
end
