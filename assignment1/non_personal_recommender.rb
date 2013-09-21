require 'set'

class NonPersonalRecommender

  def initialize
    @tastes = Hash.new
    load_matrix
  end

  def load_matrix
    matrix = File.new "recsys-data-ratings.csv", "r"

    matrix.each_line do |line|
      user = line.split(",")[0].to_i
      movie = line.split(",")[1].to_i

      if @tastes[movie] then
        @tastes[movie] << user
      else
        @tastes[movie] = [user]
      end
    end
    matrix.close
  end

  def get_top5_simple_frequency movie_id
    top_movies = Hash.new

    @tastes.keys.each do |other_movie_id|
      if movie_id != other_movie_id
        top_movies[other_movie_id] = simple_frequency(movie_id, other_movie_id)
      end
    end

    top_movies.sort_by {|k,v| v}.reverse.shift(5)
  end

  def get_top5_advanced_frequency movie_id
    top_movies = Hash.new

    @tastes.keys.each do |other_movie_id|
      if movie_id != other_movie_id
        top_movies[other_movie_id] = advanced_frequency(movie_id, other_movie_id)
      end
    end

    top_movies.sort_by {|k,v| v}.reverse.shift(5)
  end


  def simple_frequency movie1, movie2
    # (movie1 and movie2) / movie1
    total_freq = pair_freq(movie1, movie2)
    freq_movie1 = @tastes[movie1].size

    ((total_freq).to_f / freq_movie1.to_f).round(3)
  end

  def advanced_frequency movie1, movie2
    # ((movie1 and movie2) / movie1) / ((!movie1 and movie2) / !movie1))
   freq1 =  simple_frequency(movie1, movie2)

   total_freq = 0
   total_not_movie1 = 0
   unique_tastes = Set.new

    @tastes.keys.each do |other_movie_id|
        unique_tastes.merge(@tastes[other_movie_id])
    end

    not_movie1 = unique_tastes - @tastes[movie1]
    not_movie1_and_movie2 = not_movie1 & @tastes[movie2]

    (freq1.to_f / (not_movie1_and_movie2.size.to_f / not_movie1.size.to_f)).round(3)
  end

  def pair_freq movie1, movie2
    intersection = @tastes[movie1] & @tastes[movie2]
    intersection.size
  end

  def adv_pair_freq movie1, movie2, other_movie
    intersection = @tastes[other_movie] & @tastes[movie2]
    intersection1 = intersection & @tastes[movie1]

    (intersection.size - intersection1.size)
  end
end

recommender = NonPersonalRecommender.new
puts "Simple --------------------------------------"
puts "24 -----------"
puts (recommender.get_top5_simple_frequency 24).join(",")
puts "585 ----------"
puts (recommender.get_top5_simple_frequency 585).join(",")
puts "22 -----------"
puts (recommender.get_top5_simple_frequency 22).join(",")

recommender = NonPersonalRecommender.new
puts "Advanced --------------------------------------"
puts "24 -----------"
puts (recommender.get_top5_advanced_frequency 24).join(",")
puts "585 ----------"
puts (recommender.get_top5_advanced_frequency 585).join(",")
puts "22 -----------"
puts (recommender.get_top5_advanced_frequency 22).join(",")

