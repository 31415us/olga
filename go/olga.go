package main

import (
    "errors"
    "fmt"
    "image"
    "image/color"
    "image/png"
    "math"
    "math/rand"
    "os"
)

const (
    RED_MASK       = 0x00FF0000
    GREEN_MASK     = 0x0000FF00
    BLUE_MASK      = 0x000000FF
    BITSPERCHANNEL = 7
    SEED           = 1024
)

type Evaluator func(int, int) int

func WrongEuclideanDistEval(c1, c2 int) int {
    var dr = ((c1 | RED_MASK) >> 16) - ((c2 | RED_MASK) >> 16)
    var dg = ((c1 | GREEN_MASK) >> 8) - ((c2 | GREEN_MASK) >> 8)
    var db = (c1 | BLUE_MASK) - (c2 | BLUE_MASK)

    return dr*dr + dg*dg + db*db
}

func EuclideanDistEval(c1, c2 int) int {
    var dr = ((c1 & RED_MASK) >> 16) - ((c2 & RED_MASK) >> 16)
    var dg = ((c1 & GREEN_MASK) >> 8) - ((c2 & GREEN_MASK) >> 8)
    var db = (c1 & BLUE_MASK) - (c2 & BLUE_MASK)

    return dr*dr + dg*dg + db*db
}

func HammingDistEval(c1, c2 int) int {
    var hamming = c1 ^ c2
    var count = 0

    for hamming != 0 {
        count = count + (hamming & 1)
        hamming = hamming >> 1
    }

    return count
}

func abs(v int) int {
    if v < 0 {
        return -v
    }

    return v
}

func max(a, b int) int {
    if a < b {
        return b
    }

    return a
}

func Chebyshev(c1, c2 int) int {
    var dr = abs(((c1 & RED_MASK) >> 16) - ((c2 & RED_MASK) >> 16))
    var dg = abs(((c1 & GREEN_MASK) >> 8) - ((c2 & GREEN_MASK) >> 8))
    var db = abs((c1 & BLUE_MASK) - (c2 & BLUE_MASK))

    return max(dr, max(dg, db))
}

func TaxiCab(c1, c2 int) int {
    var dr = abs(((c1 & RED_MASK) >> 16) - ((c2 & RED_MASK) >> 16))
    var dg = abs(((c1 & GREEN_MASK) >> 8) - ((c2 & GREEN_MASK) >> 8))
    var db = abs((c1 & BLUE_MASK) - (c2 & BLUE_MASK))

    return dr + dg + db
}

func Minkowski(c1, c2 int) int {
    var p = 3.0
    var dr = float64(abs(((c1 & RED_MASK) >> 16) - ((c2 & RED_MASK) >> 16)))
    var dg = float64(abs(((c1 & GREEN_MASK) >> 8) - ((c2 & GREEN_MASK) >> 8)))
    var db = float64(abs((c1 & BLUE_MASK) - (c2 & BLUE_MASK)))

    return int(math.Pow(math.Pow(dr, p)+math.Pow(dg, p)+math.Pow(db, p), 1/p))
}

func Jaccard(c1, c2 int) int {
    var index = float64(c1&c2) / float64(c1|c2)

    return int(10000 * index)
}

func Hellinger(c1, c2 int) int {
    var dr = math.Sqrt(float64((c1&RED_MASK)>>16)) - math.Sqrt(float64((c2&RED_MASK)>>16))
    var dg = math.Sqrt(float64((c1&GREEN_MASK)>>8)) - math.Sqrt(float64((c2&GREEN_MASK)>>8))
    var db = math.Sqrt(float64(c1&BLUE_MASK)) - math.Sqrt(float64(c2&BLUE_MASK))

    var sum = dr*dr + dg*dg + db*db

    return int((1 / math.Sqrt(2.0)) * math.Sqrt(sum))
}

func MinEvaluator(pxList []int, neighbours []int, color int, eval Evaluator) int {

    var minVal = math.MaxInt64
    var currentColor = -1
    var currentVal = math.MaxInt64
    for _, index := range neighbours {
        currentColor = pxList[index]
        if currentColor < 0 {
            continue
        }

        currentVal = eval(color, currentColor)

        if currentVal < minVal {
            minVal = currentVal
        }
    }

    return minVal
}

func convertIntToNRGBA(c int) color.NRGBA {
    var red = (uint8)((c & RED_MASK) >> 16)
    var green = (uint8)((c & GREEN_MASK) >> 8)
    var blue = (uint8)(c & BLUE_MASK)

    return color.NRGBA{red, green, blue, 255}
}

func getDimensions(bitsPerChannel uint) (int, int) {
    var nbBits = 3 * bitsPerChannel
    var shorter = nbBits / 2
    var longer = nbBits - shorter

    return (1 << longer), (1 << shorter)
}

func initColors(bitsPerChannel uint, rng *rand.Rand) []int {

    var colors = make([]int, (1 << (3 * BITSPERCHANNEL)))

    var channelMask = (1 << bitsPerChannel) - 1
    var additionalShift = 8 - bitsPerChannel
    var color = 0
    var red = 0
    var green = 0
    var blue = 0
    for i := range colors {
        color = i
        red = i & channelMask
        color = color >> bitsPerChannel
        green = color & channelMask
        color = color >> bitsPerChannel
        blue = color & channelMask
        colors[i] = (red << (16 + additionalShift)) | (green << (8 + additionalShift)) | (blue << additionalShift)

    }

    for i := range colors {
        var j = rng.Intn(i + 1)
        colors[i], colors[j] = colors[j], colors[i]
    }

    return colors
}

func initPxList(bitsPerChannel uint) []int {
    var result = make([]int, (1 << (3 * bitsPerChannel)))

    for i := range result {
        result[i] = -1
    }

    return result
}

func coordToIndex(x int, y int, width int, height int) (int, error) {

    if x < 0 || x >= width || y < 0 || y >= height {
        return -1, errors.New("index out of bounds")
    }

    return (y*width + x), nil
}

func indexToCoord(index int, width int, height int) (int, int, error) {
    if index >= width*height {
        return -1, -1, errors.New("index out of bounds")
    }

    return index % width, index / width, nil
}

func setPx(pxList []int, x int, y int, c int, width int, height int) {
    var index, err = coordToIndex(x, y, width, height)
    if err != nil {
        return
    }

    pxList[index] = c
}

func getPx(pxList []int, x int, y int, width int, height int) int {

    var index, err = coordToIndex(x, y, width, height)

    if err != nil {
        return -1
    }

    return pxList[index]
}

func neighbourIndices(index int, width int, height int) []int {
    var x, y, err = indexToCoord(index, width, height)

    if err != nil {
        return make([]int, 0)
    }

    var result = make([]int, 0, 8)

    for i := -1; i <= 1; i++ {
        for j := -1; j <= 1; j++ {
            if i != 0 || j != 0 {
                var n, e = coordToIndex(x+i, y+j, width, height)
                if e == nil {
                    result = append(result, n)
                }
            }
        }
    }

    return result
}

func computeImage(colorList []int, pixelList []int, rng *rand.Rand, eval Evaluator, width int, height int) {
    fmt.Printf("start\n")
    var openSet = make(map[int]bool)
    var currentColor = -1
    var colorIndex = 0

    currentColor = colorList[colorIndex]
    colorIndex = colorIndex + 1

    var firstPixel = rng.Intn(width * height)
    pixelList[firstPixel] = currentColor
    for _, neighbourIndex := range neighbourIndices(firstPixel, width, height) {
        openSet[neighbourIndex] = true
    }

    var best = -1
    var bestVal = math.MaxInt64
    var currentVal = math.MaxInt64
    for len(openSet) != 0 {
        currentColor = colorList[colorIndex]
        colorIndex = colorIndex + 1

        for key, _ := range openSet {
            currentVal = MinEvaluator(pixelList, neighbourIndices(key, width, height), currentColor, HammingDistEval)
            if currentVal < bestVal {
                bestVal = currentVal
                best = key
            }
        }

        pixelList[best] = currentColor
        delete(openSet, best)

        for _, pixelIndex := range neighbourIndices(best, width, height) {
            if pixelList[pixelIndex] < 0 {
                openSet[pixelIndex] = true
            }
        }

        bestVal = math.MaxInt64
        currentVal = math.MaxInt64
    }
}

func main() {

    var rng = rand.New(rand.NewSource(SEED))

    var width, height = getDimensions(BITSPERCHANNEL)

    var colors = initColors(BITSPERCHANNEL, rng)

    var pxList = initPxList(BITSPERCHANNEL)

    m := image.NewNRGBA(image.Rect(0, 0, width, height))

    computeImage(colors, pxList, rng, EuclideanDistEval, width, height)

    for i := 0; i < width*height; i++ {
        var x, y, err = indexToCoord(i, width, height)
        if err != nil {
            return
        }
        m.Set(x, y, convertIntToNRGBA(pxList[i]))
    }

    img, _ := os.Create("test.png")
    defer img.Close()

    png.Encode(img, m)
}
