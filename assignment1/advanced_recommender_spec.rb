require "./non_personal_recommender"

describe NonPersonalRecommender, "#get_top5_advanced_frequency" do
  it "returns the expected example values" do
    recommender = NonPersonalRecommender.new

    example_1 = [[1891,5.69],[1892,5.65],[243,5.00],[1894,4.72],[2164,4.11]]
    example_2 = [[122,4.74],[120,3.82],[2164,3.40],[243,3.26],[1894,3.22]]
    example_3 = [[10020,4.18],[812,4.03],[7443,2.63],[9331,2.46],[786,2.39]]

    result_1 = recommender.get_top5_advanced_frequency(11)
    check_results 11, result_1, example_1

    result_2 = recommender.get_top5_advanced_frequency(121)
    check_results 121, result_2, example_2

    result_3 = recommender.get_top5_advanced_frequency(8587)
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
