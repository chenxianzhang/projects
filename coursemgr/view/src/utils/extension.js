
Array.prototype.in_array = function (element) {
　for (var i = 0; i < this.length; i++) {
  　if (this[i] == element) {
  　　 return true;
    }
  }
  return false;
}

String.prototype.trim = function () {
  return this.replace(/^\s+|\s+$/g, "");
}
