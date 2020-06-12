# DynamicShop-Graph
Spigot Plugin to record and graph data from the DynamicShop plugin

## Features
 * Record the price, stock, and median of an item in a shop in a CSV file.
   * Set what items in what shop to record in config.yml
   * One CSV file per item tracked
 * Write a graph image jpeg of time on the X axis and price, median, or stock on the Y axis
   * Optional. You can create as many graph images as you like, even multiple per item tracked with different settings
   * Configure the length of the graphs in config.yml, ie the last 30 minutes or the last 3 hours
   * Graphs update automatically, set interval in config.yml
