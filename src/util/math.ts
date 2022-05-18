export function toPercent(point: number): string {
  var str = Number(point * 100).toFixed(2);
  str += "%";
  return str;
}
