# tutorial
library(ggplot2)
library(data.table)

results = data.table(read.csv("analysis/eval-results.csv"))

print (ggplot(results[,list(Coverage=mean(Coverage)),by=list(Algorithm)]) + aes(x=Coverage, y=Algorithm) + geom_point())
print (ggplot(results[,list(RMSE.ByRating=mean(RMSE.ByRating)),by=list(Algorithm)]) + aes(x=RMSE.ByRating, y=Algorithm) + geom_point())
print (ggplot(results[,list(RMSE.ByUser=mean(RMSE.ByUser)),by=list(Algorithm)]) + aes(x=RMSE.ByUser, y=Algorithm) + geom_point())
print (ggplot(results[,list(nDCG=mean(nDCG)),by=list(Algorithm)]) + aes(x=nDCG, y=Algorithm) + geom_point())
print (ggplot(results[,list(TopN.nDCG=mean(TopN.nDCG)),by=list(Algorithm)]) + aes(x=TopN.nDCG, y=Algorithm) + geom_point())
print (ggplot(results[,list(TagEntropy.10=mean(TagEntropy.10)),by=list(Algorithm)]) + aes(x=TagEntropy.10, y=Algorithm) + geom_point())

# to know each NNbrs separately
print (ggplot(results[,list(TagEntropy.10=mean(TagEntropy.10)),by=list(Algorithm,NNbrs)]) + aes(y=TagEntropy.10, x=Algorithm) + geom_point(aes(colour = factor(NNbrs))))